package com.swking.util;

import com.swking.entity.User;
import org.springframework.stereotype.Component;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/16
 * @File : UserHolder
 * @Desc :
 **/
@Component
public class UserHolder {
    /**
     * 持有用户信息,用于代替session对象.
     * */
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
