package com.swking.controller;

import com.swking.entity.Event;
import com.swking.entity.User;
import com.swking.event.EventProducer;
import com.swking.service.LikeService;
import com.swking.type.ReturnData;
import com.swking.util.BlogCommunityUtil;
import com.swking.util.GlobalConstant;
import com.swking.util.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/03
 * @File : LikeController
 * @Desc :
 **/

@RestController
@Api(tags = "Like API")
public class LikeController implements GlobalConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserHolder userHolder;

   @Autowired
   private EventProducer eventProducer;

    @PostMapping(path = "/like")
    @ApiOperation(value = "点赞")
    public ReturnData like(@RequestBody Map<String, Object> params) {
        User user = userHolder.getUser();

        if(!params.containsKey("postId") || !params.containsKey("entityId") || !params.containsKey("entityUserId")){
            return ReturnData.error().message("参数错误");
        }
        int entityType=(int)params.get("entityType");
        int entityId=(int)params.get("entityId");
        int entityUserId=(int)params.get("entityUserId");
        int postId=(int)params.get("postId");
        // 点赞
        likeService.like(user.getId(), entityType, entityId, entityUserId);

        // 数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);
        // 返回的结果
        Map<String, Object> data = new HashMap<>();
        data.put("likeCount", likeCount);
        data.put("likeStatus", likeStatus);

        // 触发点赞事件
       if (likeStatus == 1) {
           Event event = new Event()
                   .setTopic(TOPIC_LIKE)
                   .setUserId(userHolder.getUser().getId())
                   .setEntityType(entityType)
                   .setEntityId(entityId)
                   .setEntityUserId(entityUserId)
                   .setData("postId", postId);
           eventProducer.fireEvent(event);
       }

        return ReturnData.success().data(data);
    }
}
