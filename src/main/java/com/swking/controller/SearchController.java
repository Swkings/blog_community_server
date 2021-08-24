package com.swking.controller;

import com.swking.entity.DiscussPost;
import com.swking.service.ElasticsearchService;
import com.swking.service.LikeService;
import com.swking.service.UserService;
import com.swking.type.Pagination;
import com.swking.type.ReturnData;
import com.swking.util.GlobalConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.naming.StringManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/06
 * @File : SearchController
 * @Desc :
 **/

@RestController
@Api(tags = "Search API")
public class SearchController implements GlobalConstant {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    // search?keyword=xxx
    @GetMapping(path = "/search")
    @ApiOperation("搜索")
    public ReturnData search(String keyword, Pagination page) {
        // 搜索帖子, page避免产生歧义
        org.springframework.data.domain.Page<DiscussPost> searchResult =
                elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
        Map<String, Object> data = new HashMap<>();
        // 聚合数据
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (searchResult != null) {
            for (DiscussPost post : searchResult) {
                Map<String, Object> map = new HashMap<>();
                // 帖子
                map.put("post", post);
                // 作者
                map.put("user", userService.findUserById(post.getUserId()));
                // 点赞数量
                map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));

                discussPosts.add(map);
            }
        }
        data.put("postUserList", discussPosts);
        data.put("keyword", keyword);

        // 分页信息
        page.setPath("/search?keyword=" + keyword);
        page.setRows(searchResult == null ? 0 : (int) searchResult.getTotalElements());
        data.put("pagination", page);

        return ReturnData.success().data(data);
    }
}
