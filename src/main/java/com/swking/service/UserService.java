package com.swking.service;

import com.swking.dao.LoginTicketMapper;
import com.swking.dao.UserMapper;
import com.swking.entity.LoginTicket;
import com.swking.entity.User;
import com.swking.type.ResultCodeEnum;
import com.swking.util.BlogCommunityUtil;
import com.swking.util.GlobalConstant;
import com.swking.util.MailClient;
import com.swking.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/19
 * @File : UserService
 * @Desc :
 **/
@Service
public class UserService implements GlobalConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${blogCommunity.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${blogCommunity.enable-redis}")
    private Boolean enableRedis;

    @Autowired
    private RedisTemplate redisTemplate;

    public User findUserById(int id){
        /**
         * 从缓存中取值
         * */
        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }

    public User findUserByName(String username){
        return userMapper.selectByName(username);
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null) {
            map.put("error", ResultCodeEnum.ERROR_NONE_FORM);
           // throw new IllegalArgumentException("参数不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("error", ResultCodeEnum.ERROR_NONE_USER);
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("error", ResultCodeEnum.ERROR_NONE_PASSWORD);
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("error", ResultCodeEnum.ERROR_NONE_EMAIL);
            return map;
        }

        // 验证账号
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("error", ResultCodeEnum.ERROR_EXISTS_USERNAME);
            return map;
        }

        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("error", ResultCodeEnum.ERROR_EXISTS_EMAIL);
            return map;
        }

        // 注册用户
        user.setSalt(BlogCommunityUtil.GenerateUUID().substring(0, 5));
        user.setPassword(BlogCommunityUtil.GetMD5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(BlogCommunityUtil.GenerateUUID());
        user.setHeaderUrl("http://swking.cn/images/avatar.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/api/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            clearCache(userId);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("error", ResultCodeEnum.ERROR_NONE_USER);
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("error", ResultCodeEnum.ERROR_NONE_PASSWORD);
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("error", ResultCodeEnum.ERROR_NOT_EXISTS_USER);
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("error", ResultCodeEnum.ERROR_NOT_ACTIVATE_USER);
            return map;
        }

        // 验证密码
        password = BlogCommunityUtil.GetMD5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("error", ResultCodeEnum.ERROR_PASSWORD);
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(BlogCommunityUtil.GenerateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));

        if(!enableRedis) {
            loginTicketMapper.insertLoginTicket(loginTicket);
        }else{
            String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
            redisTemplate.opsForValue().set(redisKey, loginTicket);
        }

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        if(!enableRedis) {
            loginTicketMapper.updateStatus(ticket, 1);
        }else {
            String redisKey = RedisKeyUtil.getTicketKey(ticket);
            LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
            loginTicket.setStatus(1);
            redisTemplate.opsForValue().set(redisKey, loginTicket);
        }
    }

    public LoginTicket findLoginTicket(String ticket) {
        if(!enableRedis){
            return loginTicketMapper.selectByTicket(ticket);
        }else{
            String redisKey = RedisKeyUtil.getTicketKey(ticket);
            return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        }
    }

    public int updateHeader(int userId, String headerUrl) {
        int rows = userMapper.updateHeader(userId, headerUrl);
        clearCache(userId);
        return rows;
    }

    // 1.优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // 2.取不到时初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    // 3.数据变更时清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }
}
