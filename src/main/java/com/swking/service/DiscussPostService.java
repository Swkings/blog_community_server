package com.swking.service;

import com.swking.dao.DiscussPostMapper;
import com.swking.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : DiscussPostService
 * @Desc :
 **/

@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper DPM;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return DPM.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId) {
        return DPM.selectDiscussPostRows(userId);
    }

}
