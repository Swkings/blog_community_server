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
 * @File : Comment
 * @Desc :
 **/

@Data
@ApiModel(value = "评论说明")
@TableName(value = "comment")
public class Comment {
    @ApiModelProperty(value = "帖子id")
    @TableId(value = "id", type = IdType.AUTO)//指定自增策略
    private int id;
    @ApiModelProperty(value = "用户ID")
    private int userId;
    @ApiModelProperty(value = "评论类型 => 1: 对帖子评论; 2: 对评论评论")
    private int entityType;
    @ApiModelProperty(value = "被评论的comment ID")
    private int entityId;
    @ApiModelProperty(value = "被评论的实体所属的用户ID")
    private int targetId;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "状态")
    private int status;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
