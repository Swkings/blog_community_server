package com.swking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swking.entity.User;

import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/14
 * @File : UserService
 * @Desc :
 **/
public interface IUserService extends IService<User> {

    Map<String, Object> login(String userName, String password, int expiredSeconds);

    void logout(String ticket);

    int activation(int userId, String code);
}
