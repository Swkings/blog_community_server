package com.swking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/13
 * @File : LoginTicket
 * @Desc : 登录凭证，持久化
 **/

@Data
@ApiModel(value = "登录凭证")
@TableName("login_ticket")
public class LoginTicket {
    @ApiModelProperty(value = "凭证ID")
    @TableId(value = "id", type = IdType.AUTO)//指定自增策略
    private Integer id;
    @ApiModelProperty(value = "用户ID")
    private int userId;
    @ApiModelProperty(value = "凭证")
    private String ticket;
    @ApiModelProperty(value = "状态")
    private int status;
    @ApiModelProperty(value = "过期时间")
    private Date expired;
}
