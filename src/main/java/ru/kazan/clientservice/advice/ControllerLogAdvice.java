package ru.kazan.clientservice.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;


@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class ControllerLogAdvice {

    private final ObjectMapper objectMapper;

    @Pointcut("execution(* ru.kazan.clientservice.controller.*.*(..))")
    public void controllerMethods() {

    }

    /* todo: Old method need to refactor

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
     */


    @Around("controllerMethods()")
    @SneakyThrows
    public Object logAroundWithException(ProceedingJoinPoint point){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        Map<String, String[]> params = request.getParameterMap();
        List<String> body = makeBodyFromArgs(point.getArgs());
        Object proceed = point.proceed();

        log.info("""
                           
                           \033[1;97mURL:\033[0m \033[1;36m{}\033[0m
                           \033[1;97mHTTP Method:\033[0m \033[1;32m{}\033[0m
                           \033[1;97mParams:\033[0m \033[1;33m{}\033[0m
                           \033[1;97mBody:\033[0m \033[1;33m{}\033[0m
                           \033[1;97mService Method:\033[0m \033[1;33m{}\033[0m\
                        """,
                request.getRequestURL(), request.getMethod(), params, body, point.getSignature().getName());

        if(proceed != null) {
            String response = makeBodyFromObject(proceed);
            log.info("""
                            
                            \033[1;97mResponse URI:\033[0m \033[1;36m{}\033[0m
                            \033[1;97mBody:\033[0m \033[1;33m{}\033[0m\
                    """, request.getRequestURI(), response);
            return proceed;
        }

        log.info("""
                            
                            \033[1;97mResponse URI:\033[0m \033[1;36m{}\033[0m\
                    """, request.getRequestURI());

        return null;
    }

    @SneakyThrows
    private String makeBodyFromObject(Object o) {
        return objectMapper.writeValueAsString(o);
    }

    private List<String> makeBodyFromArgs(Object[] args){
        List<String> reponceList = new ArrayList<>();

        Arrays.stream(args)
                .filter(Objects::nonNull)
                .forEach(object -> reponceList.add(makeBodyFromObject(object)));

        return reponceList;
    }


}
