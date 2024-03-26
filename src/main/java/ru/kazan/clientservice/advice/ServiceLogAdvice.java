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
        log.debug("\033[1;97mThe execution of the service method \033[1;33m{} \033[1;97mhas started", joinPoint.getSignature().getName());
    }

    @After("serviceMethods()")
    public void logAfter(JoinPoint joinPoint){
        log.debug("\033[1;97mThe execution of the service method \033[1;33m{} \033[1;97mhas ended", joinPoint.getSignature().getName());
    }

}
