package com.swking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swking.entity.DiscussPost;

import java.util.List;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/14
 * @File : IDiscussPostService
 * @Desc :
 **/
public interface IDiscussPostService extends IService<DiscussPost> {
    List<DiscussPost> findDiscussPosts(int userId, int offset, int limit);
    int findDiscussPostRows(int userId);
}
