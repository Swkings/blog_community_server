package com.swking.event;

import com.alibaba.fastjson.JSONObject;
import com.swking.entity.TSPEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/25
 * @File : TSPEventProducer
 * @Desc :
 **/

@Component
public class TSPEventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;
    public void fireTSP(TSPEvent tspEvent) {
        // 将事件发布到指定的主题
        kafkaTemplate.send(tspEvent.getTopic(), JSONObject.toJSONString(tspEvent));
    }
}
