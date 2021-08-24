package com.swking.type;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/23
 * @File : Node
 * @Desc :
 **/

@Data
@ApiModel(value = "Node")
@AllArgsConstructor
public class Node {
    private Integer id;
    private String name;
    private double x;
    private double y;
}
