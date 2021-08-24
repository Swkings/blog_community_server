package com.swking.config;

import com.swking.type.ResultCodeEnum;
import com.swking.type.ReturnData;
import com.swking.util.BlogCommunityUtil;
import com.swking.util.GlobalConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/17
 * @File : SecurityConfig
 * @Desc :
 **/

@Slf4j
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements GlobalConstant {

    @Value("${blogCommunity.client.domain}")
    private String clientDomain;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 授权
        http.authorizeRequests()
                .antMatchers(
                        "/user/setting",
                        "/user/upload",
                        "/community/discuss/add",
                        "/comment/add/**",
                        "/message/letter/**",
                        "/message/notice/**",
                        "/like",
                        "/follow",
                        "/unfollow"
                )
                .hasAnyAuthority(
                        AUTHORITY_USER,
                        AUTHORITY_ADMIN,
                        AUTHORITY_MODERATOR
                )
                .antMatchers(
                        "/community/top",
                        "/community/wonderful"
                )
                .hasAnyAuthority(
                        AUTHORITY_MODERATOR
                )
                .antMatchers(
                        "/community/delete",
                        "/data/**"
                )
                .hasAnyAuthority(
                        AUTHORITY_ADMIN
                )
                .anyRequest().permitAll()
                .and().csrf().disable();

        // 权限不够时的处理
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    // 没有登录
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestedWith)) {
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            // writer.write(BlogCommunityUtil.GetJSONString(403, "你还没有登录哦!"));
                            writer.write(BlogCommunityUtil.GetObjectString(ReturnData.error(ResultCodeEnum.ERROR_UN_LOGIN)));
                        } else {
                            System.out.println("=====Page=======");
                            // response.sendRedirect(request.getContextPath() + "/login");
                            log.info("访问默认页，重定向至: "+ clientDomain + "/login");
                            response.sendRedirect(clientDomain + "/login");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    // 权限不足
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestedWith)) {
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            // writer.write(BlogCommunityUtil.GetJSONString(403, "你没有访问此功能的权限!"));
                            writer.write(BlogCommunityUtil.GetObjectString(ReturnData.error(ResultCodeEnum.ERROR_PERMISSION_DENIED)));
                        } else {
                            System.out.println("=====Page=======");
                            log.info("访问默认页，重定向至: "+ clientDomain + "/denied");
                            response.sendRedirect(clientDomain + "/denied");
                        }
                    }
                });

        // Security底层默认会拦截/logout请求,进行退出处理.
        // 覆盖它默认的逻辑,才能执行我们自己的退出代码.
        http.logout().logoutUrl("/logoutCopy");
    }

}
