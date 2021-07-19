package com.swking.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swking.dao.DiscussPostMapper;
import com.swking.entity.DiscussPost;
import com.swking.util.SensitiveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/19
 * @File : DiscussPostService
 * @Desc :
 **/
@Service
public class DiscussPostService extends ServiceImpl<DiscussPostMapper, DiscussPost> {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveUtil sensitiveUtil;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveUtil.filter(post.getTitle()));
        post.setContent(sensitiveUtil.filter(post.getContent()));

        return discussPostMapper.insert(post);
    }

    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectById(id);
    }

    public int updateCommentCount(int id, int commentCount) {
        UpdateWrapper<DiscussPost> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("comment_count", commentCount);

        return discussPostMapper.update(null, updateWrapper);
    }
}
