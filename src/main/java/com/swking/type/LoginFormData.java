package com.swking.type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/13
 * @File : LoginFormData
 * @Desc :
 **/

@Setter
@ToString
@ApiModel(value = "LoginFormData")
public class LoginFormData{
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "验证码")
    private String captchaCode;
    @ApiModelProperty(value = "记住我")
    private boolean remember;
    @ApiModelProperty(value = "验证码URL")
    private String captchaUrl;

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    public boolean getRemember() {
        return remember;
    }

    public String getCaptchaUrl() {
        return captchaUrl;
    }
}