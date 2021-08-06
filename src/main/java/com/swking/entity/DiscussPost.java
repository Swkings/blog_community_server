package com.swking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
@Document(indexName = "discusspost", type = "_doc",shards = 6, replicas = 3)
public class DiscussPost {
    @Id
    @ApiModelProperty(value = "帖子id")
    @TableId(value = "id", type = IdType.AUTO)//指定自增策略
    private Integer id;

    @Field(type = FieldType.Integer)
    @ApiModelProperty(value = "帖子所属用户")
    private int userId;

    // 互联网校招, 存的时候用ik_max_word，尽可能分多的词，搜索的时候用ik_smart，智能分词
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    @ApiModelProperty(value = "标题")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    @ApiModelProperty(value = "内容")
    private String content;

    @Field(type = FieldType.Integer)
    @ApiModelProperty(value = "类型 => 0: 普通; 1: 置顶")
    private int type;

    @Field(type = FieldType.Integer)
    @ApiModelProperty(value = "状态 => 0: 正常; 1: 精华; 2: 拉黑")
    private int status;

    @Field(type = FieldType.Date)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Field(type = FieldType.Integer)
    @ApiModelProperty(value = "评论数量")
    private int commentCount;

    @Field(type = FieldType.Double)
    @ApiModelProperty(value = "评分")
    private double score;
}
