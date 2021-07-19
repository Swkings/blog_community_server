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
 * @Date : 2021/07/14
 * @File : Message
 * @Desc :
 **/

@Data
@ApiModel(value = "消息说明")
@TableName(value = "message")
public class Message {
    @ApiModelProperty(value = "用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "发消息用户ID")
    private int fromId;
    @ApiModelProperty(value = "收消息用户ID")
    private int toId;
    @ApiModelProperty(value = "会话ID => DESC(fromId_toId)")
    private String conversationId;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "状态")
    private int status;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
