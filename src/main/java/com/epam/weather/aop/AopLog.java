package com.epam.weather.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component // 切面类加入容器中
@Slf4j
public class AopLog {
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    // 定义切点
    @Pointcut("execution(public * com.epam.weather.controller..*.*(..))")
    public void aopWebLog() {
    }

    @Before("aopWebLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("URL:" + request.getRequestURL().toString());
        log.info("HTTP方法:" + request.getMethod());
        log.info("IP地址:" + request.getRemoteAddr());
        log.info("类的方法:" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("参数:" + request.getQueryString());
    }

    @AfterReturning(pointcut = "aopWebLog()", returning = "retObject")
    public void doAfterReturning(Object retObject) {
        log.info("应答值:" + retObject);
        log.info("费时:" + (System.currentTimeMillis() - startTime.get()));
    }

    @AfterThrowing(pointcut = "aopWebLog()", throwing = "ex")
    public void addAfterThrowingLogger(Exception ex) {
        log.error("执行 " + " 异常", ex);
    }
}
