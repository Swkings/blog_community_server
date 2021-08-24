package com.swking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName tsp_task
 */
@Data
@TableName(value ="tsp_task")
@ApiModel(value = "TspTask")
@Accessors(chain = true)
public class TspTask implements Serializable {
    @ApiModelProperty(value = "任务id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "任务名")
    private String name;

    @ApiModelProperty(value = "任务描述")
    private String comment;

    @ApiModelProperty(value = "任务类型：TSP, ATSP..")
    private String type;

    @ApiModelProperty(value = "问题规模")
    private Integer dimension;

    @ApiModelProperty(value = "权重类型：EUC_2D，ATT..")
    private String edgeWeightType;

    @ApiModelProperty(value = "存储路径")
    private String filePath;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "状态：0：私有，1：公开")
    private Integer status;

    @ApiModelProperty(value = "任务完成进度：0-100")
    private Integer progress;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}