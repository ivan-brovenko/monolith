package com.ivanbr.monolith.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    @Before("within(com.ivanbr.monolith.service.impl.*) " +
            "|| within(com.ivanbr.monolith.service.tmdb.impl.*)")
    public void before(JoinPoint joinPoint) {
        final Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
        String methodName = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        logger.info("AOP: Start {}() with args: {}", methodName, args);
    }

    @AfterReturning(pointcut = "within(com.ivanbr.monolith.service.impl.*)" +
            "|| within(com.ivanbr.monolith.service.tmdb.impl.*)", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Logger logger = LoggerFactory.getLogger(methodSignature.getDeclaringType());
        String methodName = methodSignature.getName();

        if (methodSignature.getReturnType().getName().contains("void")) {
            logger.info("AOP: End of {}()", methodName);
        } else {
            logger.info("AOP: End {}() with a result: {}", methodName, result);
        }
    }
}
