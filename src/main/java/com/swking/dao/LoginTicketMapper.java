package com.swking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swking.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/13
 * @File : LoginTicketMapper
 * @Desc :
 **/

@Mapper
public interface LoginTicketMapper extends BaseMapper<LoginTicket> {
    int insertLoginTicket(LoginTicket loginTicket);
    LoginTicket selectByTicket(String ticket);
    int updateStatus(String ticket, int status);
}
