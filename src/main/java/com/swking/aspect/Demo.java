package com.swking.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/01
 * @File : Demo
 * @Desc :
 **/

//@Component
//@Aspect
public class Demo {

    @Pointcut("execution(* com.swking.service.*.*(..))")
    public void pointcut() {
        /**
         * 定义切点
         * T fun(arg..)
         * execution(* com.swking.service.*.*(..)) : * 返回值， service包下所有类的所有方法，的所有参数
         * */
    }

    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }

    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }

    @AfterReturning("pointcut()")
    public void afterRetuning() {
        System.out.println("afterRetuning");
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        Object obj = joinPoint.proceed();
        System.out.println("around after");
        return obj;
    }
}
