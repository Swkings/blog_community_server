package com.swking.dao;

import com.swking.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : DiscussPostMapper
 * @Desc :
 **/

@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在mapper.xml的<if>里使用,则必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId);
}
