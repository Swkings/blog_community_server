package com.swking.type;

import lombok.Getter;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/14
 * @File : ResultCodeEnum
 * @Desc :
 **/

@Getter
public enum ResultCodeEnum {
    SUCCESS(true,20,"成功"),
    SUCCESS_NO_MESSAGE(true,20,""),
    SUCCESS_LOGIN(true,20,"登陆成功!"),
    SUCCESS_REGISTER(true,20,"注册成功!"),
    ERROR(false,30,"错误"),
    UNKNOWN_REASON(false,31,"未知错误"),
    ERROR_JSON_PARSE(false,32,"json解析异常"),
    ERROR_PARAM(false,33,"参数不正确"),
    ERROR_FILE_UPLOAD(false,34,"文件上传错误"),
    ERROR_SERVE_EXCEPTION(false,35,"服务器异常"),
    ERROR_CAPTCHA_CODE(false,40,"验证码错误!"),
    ERROR_ACTIVATE_CODE(false,41,"激活码错误!"),
    ERROR_PASSWORD(false,42,"密码错误!"),
    ERROR_UN_LOGIN(false,43,"未登录!"),
    ERROR_PERMISSION_DENIED(false,44,"权限不足!"),
    ERROR_NOT_EXISTS_USER(false,50,"用户不存在!"),
    ERROR_NOT_ACTIVATE_USER(false,51,"账号未激活!"),
    ERROR_REPEAT_ACTIVATE_USER(false,52,"账号重复激活!"),
    ERROR_NONE_FORM(false,60,"表单不能为空!"),
    ERROR_NONE_USER(false,61,"账号不能为空!"),
    ERROR_NONE_PASSWORD(false,62,"密码不能为空!"),
    ERROR_NONE_EMAIL(false,63,"邮箱不能为空!"),
    ERROR_EXISTS_USERNAME(false,70,"用户名已被注册!"),
    ERROR_EXISTS_EMAIL(false,71,"邮箱已被注册!"),
;
    private Boolean status; //响应是否成功
    private Integer code; //返回码
    private String message; //返回消息

    ResultCodeEnum(Boolean status, Integer code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
