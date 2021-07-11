package com.swking.service;

import com.swking.dao.UserMapper;
import com.swking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : UserService
 * @Desc :
 **/

@Service
public class UserService {
    @Autowired
    private UserMapper UM;

    public User findUserById(int id){
        return UM.selectById(id);
    }
}
