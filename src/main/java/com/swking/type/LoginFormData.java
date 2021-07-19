package com.swking.type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/13
 * @File : LoginFormData
 * @Desc :
 **/

@Data
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
}