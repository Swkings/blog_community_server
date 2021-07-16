package com.swking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swking.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/14
 * @File : MessageMapper
 * @Desc :
 **/
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
