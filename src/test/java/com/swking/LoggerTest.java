package com.swking;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : LoggerTest
 * @Desc :
 **/

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BlogCommunityServerApplication.class)
public class LoggerTest {
//    使用@Slf4j后，会自动生成该静态实例
//    private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void testLogger() {
        System.out.println(log.getName());
        log.debug("debug log");
        log.info("info log");
        log.warn("warn log");
        log.error("error log");
    }
}
