package com.swking.controller;


import com.swking.entity.Event;
import com.swking.entity.User;
import com.swking.event.EventProducer;
import com.swking.service.FollowService;
import com.swking.service.UserService;
import com.swking.type.Pagination;
import com.swking.type.ReturnData;
import com.swking.util.BlogCommunityUtil;
import com.swking.util.GlobalConstant;
import com.swking.util.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/04
 * @File : FollowController
 * @Desc :
 **/

@RestController
@Api(tags = "Follow API")
public class FollowController implements GlobalConstant {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private UserService userService;

   @Autowired
   private EventProducer eventProducer;

    @PostMapping(path = "/follow")
    @ApiOperation("关注")
    public ReturnData follow(int entityType, int entityId) {
        User user = userHolder.getUser();

        followService.follow(user.getId(), entityType, entityId);

       // 触发关注事件
       Event event = new Event()
               .setTopic(TOPIC_FOLLOW)
               .setUserId(userHolder.getUser().getId())
               .setEntityType(entityType)
               .setEntityId(entityId)
               .setEntityUserId(entityId);
       eventProducer.fireEvent(event);
        return ReturnData.success().message("已关注");
    }

    @PostMapping(path = "/unfollow")
    @ApiOperation("取关")
    public ReturnData unfollow(int entityType, int entityId) {
        User user = userHolder.getUser();

        followService.unfollow(user.getId(), entityType, entityId);

        return ReturnData.success().message("取关成功");
    }

    @GetMapping(path = "/followees/{userId}")
    @ApiOperation("我关注的人")
    public ReturnData getFollowees(@PathVariable("userId") int userId, Pagination page) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);

        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId, ENTITY_TYPE_USER));

        List<Map<String, Object>> userList = followService.findFollowees(userId, page.getOffset(), page.getLimit());
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        data.put("followees", userList);
        data.put("pagination", page);

        return ReturnData.success().data(data);
    }

    @GetMapping(path = "/followers/{userId}")
    @ApiOperation("关注我的人")
    public ReturnData getFollowers(@PathVariable("userId") int userId, Pagination page) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);

        // page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER, userId));

        List<Map<String, Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        data.put("followers", userList);
        data.put("pagination", page);

        return ReturnData.success().data(data);
    }

    private boolean hasFollowed(int userId) {
        if (userHolder.getUser() == null) {
            return false;
        }

        return followService.hasFollowed(userHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
    }
}
