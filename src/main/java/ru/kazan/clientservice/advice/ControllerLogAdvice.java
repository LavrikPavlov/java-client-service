package ru.kazan.clientservice.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Slf4j
@Aspect
@Component
public class ControllerLogAdvice {

    @Pointcut("execution(* ru.kazan.clientservice.controller.*.*(..))")
    public void controllerMethods() {

    }

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        log.info("""

                           \033[1;97mURL:\033[0m \033[1;36m{}\033[0m
                           \033[1;97mHTTP Method:\033[0m \033[1;32m{}\033[0m
                           \033[1;97mService Method:\033[0m \033[1;33m{}\033[0m\
                        """,
                request.getRequestURL(), request.getMethod(), joinPoint.getSignature().getName());

    }
}
