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
 * @File : DiscussPost
 * @Desc :
 **/

@Data
@ApiModel(value = "帖子信息")
@TableName(value = "discuss_post")
public class DiscussPost {
    @ApiModelProperty(value = "帖子id")
    @TableId(value = "id", type = IdType.AUTO)//指定自增策略
    private Integer id;
    @ApiModelProperty(value = "帖子所属用户")
    private int userId;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "类型 => 0: 普通; 1: 置顶")
    private int type;
    @ApiModelProperty(value = "状态 => 0: 正常; 1: 精华; 2: 拉黑")
    private int status;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "评论数量")
    private int commentCount;
    @ApiModelProperty(value = "评分")
    private double score;
}
