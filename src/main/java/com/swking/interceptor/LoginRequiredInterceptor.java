package com.swking.interceptor;

import com.swking.annotation.LoginRequired;
import com.swking.util.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/17
 * @File : LoginRequiredInterceptor
 * @Desc :
 **/

@Slf4j
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    private UserHolder userHolder;

    @Value("${blogCommunity.client.domain}")
    private String clientDomain;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断拦截的是否是方法
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if(loginRequired != null && userHolder.getUser() == null){
                // 拦截到未登录的访问，重定向到前端登录页面
                log.info("非法访问，重定向至: "+ clientDomain+"/login");
                response.sendRedirect(clientDomain+"/login");
//                System.out.println("clientDomain: "+ clientDomain);
//                System.out.println("未登录！");
                return false;
            }
        }
        return true;
    }
}
