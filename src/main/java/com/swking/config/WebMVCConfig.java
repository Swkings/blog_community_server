package com.swking.config;

import com.swking.interceptor.DataInterceptor;
import com.swking.interceptor.LoginRequiredInterceptor;
import com.swking.interceptor.LoginTicketInterceptor;
import com.swking.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/14
 * @File : WebMVCConfig
 * @Desc :
 **/

@Configuration
public class WebMVCConfig implements WebMvcConfigurer{

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
////        WebMvcConfigurer.super.addCorsMappings(registry);
//        registry.addMapping("/**")    //添加映射路径，“/**”表示对所有的路径实行全局跨域访问权限的设置
//                .allowedOrigins("*")    //开放哪些ip、端口、域名的访问权限
//                .allowCredentials(true)  //是否允许发送Cookie信息
//                .allowedMethods("GET","POST", "PUT", "DELETE")     //开放哪些Http方法，允许跨域访问
//                .allowedHeaders("*")     //允许HTTP请求中的携带哪些Header信息
//                .exposedHeaders("*") //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
//                .maxAge(3600);
//
//    }
//
//    /**
//     * 前端已设置代理跨域
//     * */
//    @Bean
//    public WebMvcConfigurer CorsConfig(){
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")    //添加映射路径，“/**”表示对所有的路径实行全局跨域访问权限的设置
//                        .allowedOrigins("*")    //开放哪些ip、端口、域名的访问权限
//                        .allowCredentials(true)  //是否允许发送Cookie信息
//                        .allowedMethods("GET","POST", "PUT", "DELETE")     //开放哪些Http方法，允许跨域访问
//                        .allowedHeaders("*")     //允许HTTP请求中的携带哪些Header信息
//                        .exposedHeaders("*") //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
//                        .maxAge(3600);
//            }
//        };
//    }

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    // 已经用security做权限控制了
    // @Autowired
    // private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                // 不拦截静态资源
                .excludePathPatterns("/**/*/*.css", "/**/*/*.js", "/**/*/*.png", "/**/*/*.jpg", "/**/*/*.jpeg");

        // registry.addInterceptor(loginRequiredInterceptor)
        //         .excludePathPatterns("/**/*/*.css", "/**/*/*.js", "/**/*/*.png", "/**/*/*.jpg", "/**/*/*.jpeg");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }
}
