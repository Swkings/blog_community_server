package com.swking.controller;

import com.swking.service.DataService;
import com.swking.type.ReturnData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/24
 * @File : DataController
 * @Desc :
 **/

@RestController
@Api(tags = "UA/DAU API")
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;

    // 统计网站UV
    @PostMapping(path = "/uv")
    public ReturnData getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        Map<String, Object> data = new HashMap<>();
        long uv = dataService.calculateUV(start, end);
        data.put("uvResult", uv);
        data.put("uvStartDate", start);
        data.put("uvEndDate", end);
        return ReturnData.success().data(data);
    }

    // 统计活跃用户
    @PostMapping(path = "/dau")
    public ReturnData getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        Map<String, Object> data = new HashMap<>();
        long dau = dataService.calculateDAU(start, end);
        data.put("dauResult", dau);
        data.put("dauStartDate", start);
        data.put("dauEndDate", end);
        return ReturnData.success().data(data);
    }
}
