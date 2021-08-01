package com.swking.controller;

import com.swking.service.DiscussPostService;
import com.swking.service.UserService;
import com.swking.type.ResultCodeEnum;
import com.swking.util.GlobalConstant;
import com.swking.entity.DiscussPost;
import com.swking.type.Pagination;
import com.swking.type.ReturnData;
import com.swking.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
public class HomeController implements GlobalConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Value("${client.domain}")
    private String clientDomain;

    @GetMapping(path = "/")
    @ApiOperation("默认页")
    public ReturnData defaultPage(Pagination page){
        return getIndexPage(page);
    }

    @GetMapping(path = "/index")
    @ApiOperation("首页")
    public ReturnData getIndexPage(Pagination page){
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> postUserList = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                postUserList.add(map);
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("pagination", page);
        data.put("postUserList", postUserList);
        return ReturnData.success(ResultCodeEnum.SUCCESS).data(data);
    }
}
