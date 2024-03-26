package ru.kazan.clientservice.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLogAdvice {

    @Pointcut("execution(* ru.kazan.clientservice.service.*.*(..))")
    public void serviceMethods(){
    }

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint){
        log.debug("The execution of the service method {} has started", joinPoint.getSignature().getName());
    }

    @After("serviceMethods()")
    public void logAfter(JoinPoint joinPoint){
        log.debug("The execution of the service method {} has ended", joinPoint.getSignature().getName());
    }

}
