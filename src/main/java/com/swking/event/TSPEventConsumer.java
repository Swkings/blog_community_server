package com.swking.event;

import com.alibaba.fastjson.JSONObject;
import com.swking.entity.TSPEvent;
import com.swking.service.TspTaskService;
import com.swking.type.TSPData;
import com.swking.util.BlogCommunityUtil;
import com.swking.util.GlobalConstant;
import com.swking.util.TSPSolver;
import com.swking.util.TSPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/25
 * @File : TSPEventConsumer
 * @Desc :
 **/

@Slf4j
@Component
public class TSPEventConsumer implements GlobalConstant {
    @Value("${blogCommunity.path.tsp-file}")
    private String tspFileDir;

    @Autowired
    private TspTaskService tspTaskService;

    @KafkaListener(topics = {TOPIC_TSP})
    public void handleSolveTSP(ConsumerRecord record){
        if (record == null || record.value() == null) {
            log.error("消息的内容为空!");
            return;
        }

        TSPEvent tspEvent = JSONObject.parseObject(record.value().toString(), TSPEvent.class);
        if(tspEvent==null){
            log.error("消息格式错误!");
            return;
        }
        TSPSolver tspSolver = new TSPSolver()
                .setTspEvent(tspEvent);
        TSPData solution = tspSolver.run();

        // 生成随机文件名
        String suffix = ".sol";
        String fileName = BlogCommunityUtil.GenerateUUID() + suffix;
        String filePath = tspFileDir+"/" + fileName;

        // 将TSP数据存文件
        TSPUtil.Object2File(filePath, solution);
        tspTaskService.updateTspTasksSolution(tspEvent.getId(), filePath, 100);
        log.info("成功解决!");
    }
}
