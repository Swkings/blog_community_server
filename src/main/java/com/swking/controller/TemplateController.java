package com.swking.controller;

import com.swking.service.UserService;
import com.swking.type.ResultCodeEnum;
import com.swking.type.ReturnData;
import com.swking.util.GlobalConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/16
 * @File : TemplateController
 * @Desc : 请求返回模板需要用@Controller，Vue前端返回json，用@RestController，用该类将此分离
 **/
@Controller
@Api(tags = "Template API")
public class TemplateController implements GlobalConstant {
    @Autowired
    private UserService userService;

    @Value("${client.domain}")
    private String clientDomain;

    // http://localhost:8080/api/activation/101/code
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
            resData = ReturnData.success().message("激活成功");
        } else if (result == ACTIVATION_REPEAT) {
            resData = ReturnData.error(ResultCodeEnum.ERROR_REPEAT_ACTIVATE_USER);
        } else {
            resData = ReturnData.error(ResultCodeEnum.ERROR_ACTIVATE_CODE);
        }
        model.addAttribute("url", clientDomain+"/login");
        model.addAttribute("resData", resData);
        return "/transfer/activation-result";
    }
}
