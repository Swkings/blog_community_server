package com.swking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swking.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/14
 * @File : CommentMapper
 * @Desc :
 **/

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(int id);
}
