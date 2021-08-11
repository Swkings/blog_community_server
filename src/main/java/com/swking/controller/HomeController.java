package com.swking.controller;

import com.swking.util.GlobalConstant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : HomeController
 * @Desc :
 **/
@Slf4j
@RestController
@Api(tags = "Home API")
public class HomeController {

    @Value("${blogCommunity.client.domain}")
    private String clientDomain;

    @GetMapping(path = "/")
    @ApiOperation("默认页")
    public void defaultPage(HttpServletResponse response) throws IOException {
        index(response);
    }

    @GetMapping(path = "/index")
    @ApiOperation("默认页")
    public void index(HttpServletResponse response) throws IOException {
        log.info("访问默认页，重定向至: "+ clientDomain);
        response.sendRedirect(clientDomain);
    }

}
