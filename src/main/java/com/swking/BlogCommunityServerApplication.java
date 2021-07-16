package com.swking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.swking.dao")
public class BlogCommunityServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogCommunityServerApplication.class, args);
    }

}
