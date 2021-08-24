package com.swking.interceptor;

import com.swking.entity.User;
import com.swking.service.DataService;
import com.swking.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/24
 * @File : DataInterceptor
 * @Desc :
 **/

@Component
public class DataInterceptor implements HandlerInterceptor {

    @Autowired
    private DataService dataService;

    @Autowired
    private UserHolder userHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 统计UV
        String ip = request.getRemoteHost();
        dataService.recordUV(ip);

        // 统计DAU
        User user = userHolder.getUser();
        if (user != null) {
            dataService.recordDAU(user.getId());
        }

        return true;
    }
}
