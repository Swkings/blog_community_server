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
 * @Date : 2021/07/11
 * @File : User
 * @Desc :
 **/
@Data
@ApiModel(value = "用户信息")
@TableName(value = "user")
public class User {
    @ApiModelProperty(value = "用户id")
    @TableId(value = "id", type = IdType.AUTO)//指定自增策略
    private Integer id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "密码附加串")
    private String salt;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "用户类型")
    private int type;
    @ApiModelProperty(value = "状态")
    private int status;
    @ApiModelProperty(value = "激活码")
    private String activationCode;
    @ApiModelProperty(value = "头像地址")
    private String headerUrl;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
