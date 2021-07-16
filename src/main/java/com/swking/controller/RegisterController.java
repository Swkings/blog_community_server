package com.swking.controller;

import com.swking.type.ResultCodeEnum;
import com.swking.util.GlobalConstant;
import com.swking.type.ReturnData;
import com.swking.entity.User;
import com.swking.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : RegisterController
 * @Desc :
 **/
@RestController
@Api(tags = "Register API")
public class RegisterController implements GlobalConstant {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(path = "/register")
    @ApiOperation("注册")
    public ReturnData register(@RequestBody User user) {
        System.out.println(user);
        Map<String, Object> msgInfo = userService.register(user);
        if (msgInfo == null || msgInfo.isEmpty()) {
            return ReturnData.success(ResultCodeEnum.SUCCESS_REGISTER).message("注册成功,我们已经向您的邮箱发送了一封激活邮件,请尽快激活!");
        } else {
            return ReturnData.error((ResultCodeEnum) msgInfo.get("error"));
        }
    }
}
