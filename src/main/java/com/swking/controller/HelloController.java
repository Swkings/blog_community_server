package com.swking.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : HelloController
 * @Desc :
 **/

@Api(tags = "HelloController")
@RestController
public class HelloController {

    @GetMapping(value = "/hello")
    @ApiOperation("Hello接口")
    public String hello(){
        return "hello";
    }
}
