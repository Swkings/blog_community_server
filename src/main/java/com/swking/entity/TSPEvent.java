package com.swking.entity;

import com.swking.type.TSPData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/25
 * @File : TSPEvent
 * @Desc :
 **/

@Data
@Accessors(chain = true)
public class TSPEvent {
    private String topic;
    private int id;
    private TSPData tspData;
    private Map<String, Object> data = new HashMap<>();
    public TSPEvent setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
