package com.swking.advice;

import com.swking.type.ResultCodeEnum;
import com.swking.type.ReturnData;
import com.swking.util.BlogCommunityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/01
 * @File : ExceptionAdvice
 * @Desc : 统一异常处理
 **/

@Slf4j
@ControllerAdvice(annotations = Controller.class) // 扫描带Controller注解的Bean
public class ExceptionAdvice {
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("服务器发生异常: " + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }

       // 如果是异步请求，则返回json，否则重定向至error
        String xRequestedWith = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(BlogCommunityUtil.GetObjectString(ReturnData.error(ResultCodeEnum.ERROR_SERVE_EXCEPTION)));
        } else {
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
