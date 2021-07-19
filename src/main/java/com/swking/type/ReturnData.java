package com.swking.type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : ReturnData
 * @Desc :
 **/

@Data
@ApiModel(value = "ReturnData")
public class ReturnData{

    @ApiModelProperty(value = "成功标识 => true: 成功; false:失败")
    private boolean success;

    @ApiModelProperty(value = "返回码")
    private int code;

    @ApiModelProperty(value = "返回信息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String,Object> data;

    public ReturnData() {
        this.data = new HashMap<String ,Object>();
    }

    public static ReturnData success(){
        ReturnData returnData = new ReturnData();
        returnData.setCode(ResultCodeEnum.SUCCESS.getCode());
        returnData.setSuccess(ResultCodeEnum.SUCCESS.getStatus());
        returnData.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return returnData;
    }

    public static ReturnData error(){
        ReturnData returnData = new ReturnData();
        returnData.setCode(ResultCodeEnum.ERROR.getCode());
        returnData.setSuccess(ResultCodeEnum.ERROR.getStatus());
        returnData.setMessage(ResultCodeEnum.ERROR.getMessage());
        return returnData;
    }
    public static ReturnData success(ResultCodeEnum codeEnum){
        ReturnData returnData = new ReturnData();
        returnData.setCode(codeEnum.getCode());
        returnData.setSuccess(codeEnum.getStatus());
        returnData.setMessage(codeEnum.getMessage());
        return returnData;
    }

    public static ReturnData error(ResultCodeEnum codeEnum){
        ReturnData returnData = new ReturnData();
        returnData.setCode(codeEnum.getCode());
        returnData.setSuccess(codeEnum.getStatus());
        returnData.setMessage(codeEnum.getMessage());
        return returnData;
    }
    public ReturnData data(Map<String,Object> map){
        this.setData(map);
        return this;
    }

    public ReturnData data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    public ReturnData message(String message){
        this.setMessage(message);
        return this;
    }

    public ReturnData code(Integer code){
        this.setCode(code);
        return this;
    }

    public ReturnData success(Boolean success){
        this.setSuccess(success);
        return this;
    }
}