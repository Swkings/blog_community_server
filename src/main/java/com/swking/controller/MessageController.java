package com.swking.controller;

import com.alibaba.fastjson.JSONObject;
import com.swking.entity.Message;
import com.swking.entity.User;
import com.swking.service.MessageService;
import com.swking.service.UserService;
import com.swking.type.Pagination;
import com.swking.type.ResultCodeEnum;
import com.swking.type.ReturnData;
import com.swking.util.BlogCommunityUtil;
import com.swking.util.GlobalConstant;
import com.swking.util.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/19
 * @File : MessageController
 * @Desc :
 **/
@RestController
@Api(tags = "Message API")
@RequestMapping("/message")
public class MessageController implements GlobalConstant {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private UserService userService;

    // 私信列表
    @GetMapping(path = "/letter/list")
    @ApiOperation("私信列表")
    public ReturnData getLetterList(Pagination page) {
        Map<String, Object> data = new HashMap<>();
        User user = userHolder.getUser();
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));

        // 会话列表
        List<Message> conversationList = messageService.findConversations(
                user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount", messageService.findLetterUnreadCount(user.getId(), message.getConversationId()));
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(targetId));

                conversations.add(map);
            }
        }
        data.put("conversations", conversations);

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        data.put("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
        data.put("noticeUnreadCount", noticeUnreadCount);

        data.put("page", page);
        return ReturnData.success().data(data);
    }

    @GetMapping(path = "/letter/detail/{conversationId}")
    @ApiOperation("私信详情")
    public ReturnData getLetterDetail(@PathVariable("conversationId") String conversationId, Pagination page) {
        Map<String, Object> data = new HashMap<>();
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));

        // 私信列表
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        data.put("letters", letters);

        // 私信目标
        data.put("target", getLetterTarget(conversationId));

        // 设置已读
        List<Integer> ids = getLetterIds(letterList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }
        data.put("page", page);
        return ReturnData.success().data(data);
    }

    private User getLetterTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);

        if (userHolder.getUser().getId() == id0) {
            return userService.findUserById(id1);
        } else {
            return userService.findUserById(id0);
        }
    }

    private List<Integer> getLetterIds(List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();

        if (letterList != null) {
            for (Message message : letterList) {
                if (userHolder.getUser().getId() == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }

        return ids;
    }

    @PostMapping(path = "/letter/send")
    @ApiOperation("发送消息")
    public ReturnData sendLetter(String toName, String content) {
        User target = userService.findUserByName(toName);
        if (target == null) {
            return ReturnData.error(ResultCodeEnum.ERROR_NOT_EXISTS_USER);
        }

        Message message = new Message();
        message.setFromId(userHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setCreateTime(new Date());
        messageService.addMessage(message);

        return ReturnData.success(ResultCodeEnum.SUCCESS).message("发送成功");
    }

    @GetMapping(path = "/notice/list")
    @ApiOperation("通知列表")
    public ReturnData getNoticeList() {
        Map<String, Object> data = new HashMap<>();
        User user = userHolder.getUser();

        // 查询评论类通知
        Message message = messageService.findLatestNotice(user.getId(), TOPIC_COMMENT);
        Map<String, Object> messageVO = new HashMap<>();
        if (message != null) {
            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> contentMap = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", contentMap.get("entityType"));
            messageVO.put("entityId", contentMap.get("entityId"));
            messageVO.put("postId", contentMap.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_COMMENT);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_COMMENT);
            messageVO.put("unread", unread);
        }
        data.put("commentNotice", messageVO);

        // 查询点赞类通知
        message = messageService.findLatestNotice(user.getId(), TOPIC_LIKE);
        messageVO = new HashMap<>();
        if (message != null) {
            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> contentMap = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", contentMap.get("entityType"));
            messageVO.put("entityId", contentMap.get("entityId"));
            messageVO.put("postId", contentMap.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_LIKE);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_LIKE);
            messageVO.put("unread", unread);
        }
        data.put("likeNotice", messageVO);

        // 查询关注类通知
        message = messageService.findLatestNotice(user.getId(), TOPIC_FOLLOW);
        messageVO = new HashMap<>();
        if (message != null) {
            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> contentMap = JSONObject.parseObject(content, HashMap.class);

            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", contentMap.get("entityType"));
            messageVO.put("entityId", contentMap.get("entityId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_FOLLOW);
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_FOLLOW);
            messageVO.put("unread", unread);
        }
        data.put("followNotice", messageVO);

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        data.put("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
        data.put("noticeUnreadCount", noticeUnreadCount);

        return ReturnData.success().data(data);
    }

    @GetMapping(path = "/notice/detail/{topic}")
    @ApiOperation("通知详情")
    public ReturnData getNoticeDetail(@PathVariable("topic") String topic, Pagination page, Model model) {
        Map<String, Object> data = new HashMap<>();
        User user = userHolder.getUser();

        page.setLimit(5);
        page.setPath("/notice/detail/" + topic);
        page.setRows(messageService.findNoticeCount(user.getId(), topic));

        List<Message> noticeList = messageService.findNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if (noticeList != null) {
            for (Message notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                // 通知
                map.put("notice", notice);
                // 内容
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> contentMap = JSONObject.parseObject(content, HashMap.class);
                map.put("user", userService.findUserById((Integer) data.get("userId")));
                map.put("entityType", contentMap.get("entityType"));
                map.put("entityId", contentMap.get("entityId"));
                map.put("postId", contentMap.get("postId"));
                // 通知作者
                map.put("fromUser", userService.findUserById(notice.getFromId()));

                noticeVoList.add(map);
            }
        }
        data.put("notices", noticeVoList);

        // 设置已读
        List<Integer> ids = getLetterIds(noticeList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }
        data.put("page", page);

        return ReturnData.success().data(data);
    }
}
