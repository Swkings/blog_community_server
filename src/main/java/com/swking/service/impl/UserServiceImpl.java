package com.swking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swking.dao.LoginTicketMapper;
import com.swking.service.IUserService;
import com.swking.type.ResultCodeEnum;
import com.swking.util.GlobalConstant;
import com.swking.dao.UserMapper;
import com.swking.entity.LoginTicket;
import com.swking.entity.User;
import com.swking.util.BlogCommunityUtil;
import com.swking.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : UserService
 * @Desc :
 **/

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService, GlobalConstant{
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

    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null) {
            map.put("error", ResultCodeEnum.ERROR_NONE_FORM);

//            throw new IllegalArgumentException("参数不能为空!");
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
//            clearCache(userId);
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
        loginTicketMapper.insertLoginTicket(loginTicket);

//        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
//        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
//        String redisKey = RedisKeyUtil.getTicketKey(ticket);
//        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
//        loginTicket.setStatus(1);
//        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
//        String redisKey = RedisKeyUtil.getTicketKey(ticket);
//        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);
//        int rows = userMapper.updateHeader(userId, headerUrl);
//        clearCache(userId);
//        return rows;
    }
}
