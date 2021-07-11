package com.swking.controller;

import com.swking.entity.DiscussPost;
import com.swking.entity.Pagination;
import com.swking.entity.User;
import com.swking.service.DiscussPostService;
import com.swking.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : HomeController
 * @Desc :
 **/

@RestController
@Api(tags = "Home API")
public class HomeController {
    @Autowired
    private DiscussPostService DPS;

    @Autowired
    private UserService US;

    @GetMapping(path = "/index")
    @ApiOperation("首页")
    public Map<String, Object> getIndexPage(Pagination page){
        Map<String, Object> resData = new HashMap<>();
        page.setRows(DPS.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = DPS.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> postUserList = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = US.findUserById(post.getUserId());
                map.put("user", user);
                postUserList.add(map);
            }
        }
        resData.put("pagination", page);
        resData.put("postUserList", postUserList);
        return resData;
    }
}
