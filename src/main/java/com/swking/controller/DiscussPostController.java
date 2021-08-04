package com.swking.controller;

import com.swking.entity.Comment;
import com.swking.entity.DiscussPost;
import com.swking.entity.User;
import com.swking.service.CommentService;
import com.swking.service.DiscussPostService;
import com.swking.service.LikeService;
import com.swking.service.UserService;
import com.swking.type.Pagination;
import com.swking.type.ResultCodeEnum;
import com.swking.type.ReturnData;
import com.swking.util.GlobalConstant;
import com.swking.util.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/19
 * @File : DiscussPostController
 * @Desc :
 **/

@RestController
@Api(tags = "Discuss API")
@RequestMapping("/discuss")
public class DiscussPostController implements GlobalConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private LikeService likeService;

    @PostMapping(path = "/add")
    @ApiOperation(value = "发布帖子")
    public ReturnData addDiscussPost(@RequestBody DiscussPost post) {
        System.out.println(post);
        User user = userHolder.getUser();
        if (user == null) {
            return ReturnData.error(ResultCodeEnum.ERROR_UN_LOGIN);
        }

        post.setUserId(user.getId());
        post.setCreateTime(new Date());

        discussPostService.addDiscussPost(post);

//        // 触发发帖事件
//        Event event = new Event()
//                .setTopic(TOPIC_PUBLISH)
//                .setUserId(user.getId())
//                .setEntityType(ENTITY_TYPE_POST)
//                .setEntityId(post.getId());
//        eventProducer.fireEvent(event);

        // 报错的情况,将来统一处理.
        return ReturnData.error(ResultCodeEnum.SUCCESS).message("发布成功！");
    }

    @GetMapping(path = "/detail/{discussPostId}")
    @ApiOperation(value = "帖子详情")
    public ReturnData getDiscussPost(@PathVariable("discussPostId") int discussPostId, Pagination page) {
        Map<String, Object> data = new HashMap<>();
        // 帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        data.put("post", post);
        // 作者
        User user = userService.findUserById(post.getUserId());
        data.put("user", user);
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        data.put("likeCount", likeCount);
        // 点赞状态
        int likeStatus = userHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(userHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        data.put("likeStatus", likeStatus);

        // 评论分页信息
        // 分页信息会自动注入, 如果地址中带参数的话
        // 如果
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());

        // 评论: 给帖子的评论
        // 回复: 给评论的评论
        // 评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // 评论VO列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));
                // 点赞数量
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);
                // 点赞状态
                likeStatus = userHolder.getUser() == null ? 0 :
                        likeService.findEntityLikeStatus(userHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus", likeStatus);

                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);
                        // 点赞数量
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeCount", likeCount);
                        // 点赞状态
                        likeStatus = userHolder.getUser() == null ? 0 :
                                likeService.findEntityLikeStatus(userHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeStatus", likeStatus);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }

        data.put("comments", commentVoList);
        data.put("pagination", page);
        return ReturnData.success(ResultCodeEnum.SUCCESS).data(data);
    }
}
