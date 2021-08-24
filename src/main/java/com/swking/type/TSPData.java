package com.swking.type;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/23
 * @File : TSPData
 * @Desc :
 **/
@Data
@ApiModel(value = "TSPData")
@Accessors(chain = true)
public class TSPData {
    /***
     * 默认驼峰规则
     * @JsonProperty(value = "NAME") 接收大写参数时
     * 或者@JSONField(name = "NAME")
     */
    @ApiModelProperty(value = "名称")
    @JSONField(name = "NAME")
    @JsonProperty(value = "NAME")
    private String NAME;

    @ApiModelProperty(value = "备注")
    @JSONField(name = "COMMENT")
    @JsonProperty(value = "COMMENT")
    private String COMMENT;

    @ApiModelProperty(value = "问题类型")
    @JSONField(name = "TYPE")
    @JsonProperty(value = "TYPE")
    private String TYPE;

    @ApiModelProperty(value = "城市个数")
    @JSONField(name = "DIMENSION")
    @JsonProperty(value = "DIMENSION")
    private int DIMENSION;

    @ApiModelProperty(value = "权重类型")
    @JSONField(name = "EDGE_WEIGHT_TYPE")
    @JsonProperty(value = "EDGE_WEIGHT_TYPE")
    private String EDGE_WEIGHT_TYPE;

    @ApiModelProperty(value = "点集")
    @JSONField(name = "NODE_COORD_SECTION")
    @JsonProperty(value = "NODE_COORD_SECTION")
    private List<Node> NODE_COORD_SECTION;

}
