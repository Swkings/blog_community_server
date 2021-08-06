package com.swking.event;

import com.alibaba.fastjson.JSONObject;
import com.swking.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/04
 * @File : EventProducer
 * @Desc :
 **/

@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    // 处理事件
    public void fireEvent(Event event) {
        // 将事件发布到指定的主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
