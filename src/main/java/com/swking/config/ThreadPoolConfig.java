package com.swking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/24
 * @File : ThreadPoolConfig
 * @Desc : Spring默认不启动线程池，所以需要配置启动
 **/

@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
