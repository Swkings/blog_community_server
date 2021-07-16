package com.swking.controller;

import com.google.code.kaptcha.Producer;
import com.swking.service.IUserService;
import com.swking.type.ResultCodeEnum;
import com.swking.util.GlobalConstant;
import com.swking.type.LoginFormData;
import com.swking.type.ReturnData;
import com.swking.util.BlogCommunityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : LoginController
 * @Desc :
 **/

@Controller
@Api(tags = "Login API")
public class LoginController implements GlobalConstant {

    @Autowired
    private IUserService userService;

    @Autowired
    private Producer captchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @PostMapping(path = "/login")
    @ApiOperation(value = "登录", response = ReturnData.class)
    public ReturnData login(@RequestBody LoginFormData loginForm,
                          HttpSession session, HttpServletResponse response,
                          @CookieValue("captchaOwner") String captchaOwner) {
        // 检查验证码
         String captcha = (String) session.getAttribute("captcha");
        System.out.println("检查验证码："+captcha);
//        String captcha = null;
//        if (StringUtils.isNotBlank(captchaOwner)) {
//            String redisKey = RedisKeyUtil.getKaptchaKey(captchaOwner);
//            captcha = (String) redisTemplate.opsForValue().get(redisKey);
//        }
        System.out.println("loginForm："+loginForm.toString());
        if (StringUtils.isBlank(captcha) || StringUtils.isBlank(loginForm.getCaptchaCode()) || !captcha.equalsIgnoreCase(loginForm.getCaptchaCode())) {
            return ReturnData.error(ResultCodeEnum.ERROR_CAPTCHA_CODE);
        }

        // 检查账号,密码
        int expiredSeconds = loginForm.getRemember() ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> infoMap = userService.login(loginForm.getUserName(), loginForm.getPassword(), expiredSeconds);
        if (infoMap.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", infoMap.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return ReturnData.success(ResultCodeEnum.SUCCESS_LOGIN);
        } else {
            if (infoMap.containsKey("error")){
                return ReturnData.error((ResultCodeEnum) infoMap.get("error"));
            }else{
                return ReturnData.error();
            }
        }
    }

    @PostMapping(path = "/logout")
    @ApiOperation(value = "登出", response = ReturnData.class)
    public ReturnData logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return ReturnData.success().message("登出成功");
    }

    @GetMapping(path = "/captcha")
    @ApiOperation("验证码")
    public void getCaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        String text = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(text);
        System.out.println("生成验证码："+text);

//         将验证码存入session
         session.setAttribute("captcha", text);

        // 验证码的归属
        String captchaOwner = BlogCommunityUtil.GenerateUUID();
        Cookie cookie = new Cookie("captchaOwner", captchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);
        // 将验证码存入Redis
        // String redisKey = RedisKeyUtil.getCaptchaKey(CaptchaOwner);
        // redisTemplate.opsForValue().set(redisKey, text, 60, TimeUnit.SECONDS);

        // 将突图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            System.out.println("响应验证码失败:" + e.getMessage());
        }
    }

    // http://localhost:8080/community/activation/101/code
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    @ApiOperation(value = "激活账号", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "path", required = true),
            @ApiImplicitParam(name = "code", value = "激活码", paramType = "path", required = true)
    })
    public String activation(
            Model model,
            @PathVariable("userId") int userId,
            @PathVariable("code") String code,
            HttpServletRequest request, HttpServletResponse response) {
        ReturnData resData;
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            resData = ReturnData.success().message("激活成功,您的账号已经可以正常使用了!");
        } else if (result == ACTIVATION_REPEAT) {
            resData = ReturnData.error(ResultCodeEnum.ERROR_REPEAT_ACTIVATE_USER);
        } else {
            resData = ReturnData.error(ResultCodeEnum.ERROR_ACTIVATE_CODE);
        }
        model.addAttribute("url", "http://locolhost:3001/login");
        model.addAttribute("resData", resData);
        return "/transfer/activation-result";
    }
}
