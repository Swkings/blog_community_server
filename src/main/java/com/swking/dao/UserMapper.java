package com.swking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swking.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : UserMapper
 * @Desc :
 **/

@Mapper
public interface UserMapper extends BaseMapper<User> {
//    User selectById(int id);
    User selectByName(String username);
    User selectByEmail(String email);
    int insertUser(User user);
    int updateStatus(int id, int status);
    int updateHeader(int id, String headerUrl);
    int updatePassword(int id, String password);
}
