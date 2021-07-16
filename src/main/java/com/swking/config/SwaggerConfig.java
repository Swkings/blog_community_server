package com.swking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : SwaggerConfig
 * @Desc :
 **/

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Swking")
                .apiInfo(apiInfo())
                .ignoredParameterTypes(HttpSession.class, HttpServletRequest.class, HttpServletResponse.class, ModelAndView.class)
                .select()
                // RequestHandlerSelectors: 配置扫描接口的方式
                // basePackage: 指定要扫描的包  ("com.swking.controller")
                // any(): 扫描全部
                // none(): 不扫描
                // none(): 不扫描
                // withClassAnnotation(GetMapping.class): 扫描类上的注解
                // withMethodAnnotation(RestController.class): 扫描方法上的注解
                .apis(RequestHandlerSelectors.basePackage("com.swking.controller"))
                // 过滤路径
//                .paths(PathSelectors.any())
                .build()
                .globalResponseMessage(RequestMethod.GET,
                        newArrayList(new ResponseMessageBuilder()
                                        .code(500)
                                        .message("系统繁忙！")
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(200)
                                        .message("请求成功!")
                                        .build()))
                .globalResponseMessage(RequestMethod.POST,
                        newArrayList(new ResponseMessageBuilder()
                                        .code(500)
                                        .message("系统繁忙！")
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(200)
                                        .message("请求成功!")
                                        .build()))
                .globalResponseMessage(RequestMethod.DELETE,
                        newArrayList(new ResponseMessageBuilder()
                                        .code(500)
                                        .message("系统繁忙！")
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(200)
                                        .message("请求成功!")
                                        .build()));
    }

    private ApiInfo apiInfo(){
        Contact contact = new Contact("Swking", "http://swking.cn", "1114006175.qq.com");
        return new ApiInfo(
                "Swking API Doc",
                "This is a restful api document of Blog Community.",
                "1.0",
                "http://swking.cn",
                contact,
                "Apache 2.0",
                "http://www.apach.org/licenses/LICENSE-2.0",
                new ArrayList()
        );
    }
}
