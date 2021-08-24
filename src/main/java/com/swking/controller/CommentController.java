package com.swking.controller;

import com.swking.entity.Comment;
import com.swking.entity.DiscussPost;
import com.swking.entity.Event;
import com.swking.event.EventProducer;
import com.swking.service.CommentService;
import com.swking.service.DiscussPostService;
import com.swking.service.UserService;
import com.swking.type.ResultCodeEnum;
import com.swking.type.ReturnData;
import com.swking.util.GlobalConstant;
import com.swking.util.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/19
 * @File : CommentController
 * @Desc :
 **/
@RestController
@Api(tags = "Comment API")
@RequestMapping("/comment")
public class CommentController implements GlobalConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserHolder userHolder;

   @Autowired
   private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/add/{discussPostId}")
    @ApiOperation("添加帖子")
    public ReturnData addComment(@PathVariable("discussPostId") int discussPostId, @RequestBody Comment comment) {
        System.out.println("discussPostId: "+discussPostId);
        if(comment==null || discussPostId<0) {
            return ReturnData.error().message("参数错误");
        }
        comment.setUserId(userHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        System.out.println("comment: "+comment.toString());
        Map<String, Object> reply = new HashMap<>();
       // 触发评论事件
       Event event = new Event()
               .setTopic(TOPIC_COMMENT)
               .setUserId(userHolder.getUser().getId())
               .setEntityType(comment.getEntityType())
               .setEntityId(comment.getEntityId())
               .setData("postId", discussPostId);
       if (comment.getEntityType() == ENTITY_TYPE_POST) {
           DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
           event.setEntityUserId(target.getUserId());
       } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
           Comment target = commentService.findCommentById(comment.getEntityId());
           event.setEntityUserId(target.getUserId());
           reply.put("target", userService.findUserById(target.getUserId()));
       }
       eventProducer.fireEvent(event);

       if (comment.getEntityType() == ENTITY_TYPE_POST) {
           // 触发发帖事件
           event = new Event()
                   .setTopic(TOPIC_PUBLISH)
                   .setUserId(comment.getUserId())
                   .setEntityType(ENTITY_TYPE_POST)
                   .setEntityId(discussPostId);
           eventProducer.fireEvent(event);
       }

        reply.put("likeCount", 0);
        reply.put("likeStatus", 0);
        reply.put("reply", comment);
        reply.put("user", userHolder.getUser());

        return ReturnData.success(ResultCodeEnum.SUCCESS_NO_MESSAGE).data(reply);
    }
}
