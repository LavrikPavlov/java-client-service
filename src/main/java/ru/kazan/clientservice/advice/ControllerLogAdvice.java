package ru.kazan.clientservice.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerLogAdvice {

    @Pointcut("execution(* ru.kazan.clientservice.controller.*.*(..))")
    public void controllerMethods(){

    }

}