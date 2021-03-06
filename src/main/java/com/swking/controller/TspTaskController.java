package com.swking.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swking.entity.TSPEvent;
import com.swking.entity.TspTask;
import com.swking.event.TSPEventProducer;
import com.swking.service.LikeService;
import com.swking.service.TspTaskService;
import com.swking.type.ReturnData;
import com.swking.type.TSPData;
import com.swking.util.BlogCommunityUtil;
import com.swking.util.GlobalConstant;
import com.swking.util.TSPUtil;
import com.swking.util.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Wrapper;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/22
 * @File : TspTaskController
 * @Desc :
 **/

@RestController
@Api(tags = "TSP Solver API")
@RequestMapping("/tsp-solver")
public class TspTaskController implements GlobalConstant {

    @Value("${blogCommunity.path.tsp-file}")
    private String tspFileDir;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private TspTaskService tspTaskService;

    @Autowired
    private TSPEventProducer tspEventProducer;

    @GetMapping(path = "/index")
    @ApiOperation("index")
    public ReturnData index(){
        return myTask();
    }

    @PostMapping(path = "/addTask")
    @ApiOperation("添加任务")
    // @RequestParam(value = "tspFile", required = false) MultipartFile tspFile formData和json不能同时在一个接口
    public ReturnData addTask(@RequestBody TSPData tspData){
        System.out.println(tspData);
        // 生成随机文件名
        String suffix = ".tsp";
        String fileName = BlogCommunityUtil.GenerateUUID() + suffix;
        String filePath = tspFileDir+"/" + fileName;

        // 将TSP数据存文件
        TSPUtil.Object2File(filePath, tspData);
        // 构造任务数据，存数据库
        TspTask tspTask = new TspTask()
                .setName(tspData.getNAME())
                .setComment(tspData.getCOMMENT())
                .setType(tspData.getTYPE())
                .setDimension(tspData.getDIMENSION())
                .setEdgeWeightType(tspData.getEDGE_WEIGHT_TYPE())
                .setFilePath(filePath)
                .setUserId(userHolder.getUser().getId())
                .setStatus(0)
                .setProgress(0)
                .setCreateTime(new Date());

        tspTaskService.save(tspTask);
        System.out.println(tspTask);
        // TODO: 开始任务
        TSPEvent tspEvent = new TSPEvent()
                .setTopic(TOPIC_TSP)
                .setId(tspTask.getId())
                .setTspData(tspData);
        tspEventProducer.fireTSP(tspEvent);

        return ReturnData.success().data("tspData", tspData);
    }

    @GetMapping(path = "/myTask")
    @ApiOperation("任务列表")
    public ReturnData myTask(){
        List<TspTask> list = tspTaskService.findTspTasks(userHolder.getUser().getId());
        return ReturnData.success().data("taskList", list);
    }

    @GetMapping(path = "/deleteTask/{id}")
    @ApiOperation("删除任务")
    public ReturnData deleteTask(@PathVariable("id") int id){
        if(tspTaskService.deleteTask(id)==1){
            return ReturnData.success().message("删除成功");
        }else{
            return ReturnData.error().message("删除失败");
        }
    }

    @GetMapping(path = "/viewSolution/{id}")
    @ApiOperation("查看结果")
    public ReturnData viewSolution(@PathVariable("id") int id){
        TspTask tspTask = tspTaskService.getById(id);
        if(tspTask.getProgress()<100){
            return ReturnData.error().message("任务未完成");
        }
        TSPData tspData = TSPUtil.File2Object(tspTask.getSolutionFilePath(), TSPData.class);
        return ReturnData.success().data("tspData", tspData);
    }



}
