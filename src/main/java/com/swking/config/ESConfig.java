package com.swking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/06
 * @File : ESConfig
 * @Desc :
 **/

public class ESConfig {

    // @Value("${blogCommunity.elasticsearch.url}")
    // private String esURL;
    //
    // @Bean
    // RestHighLevelClient client() {
    //     ClientConfiguration clientConfiguration = ClientConfiguration.builder()
    //             .connectedTo(esURL)
    //             .build();
    //
    //     return RestClients.create(clientConfiguration).rest();
    // }
}
