package com.duckpao.order.aop;

import com.duckpao.order.model.ApiLog;
import com.duckpao.order.repository.ApiLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect {

    private final ApiLogRepository apiLogRepository;
    private final ObjectMapper objectMapper;

    @Around("execution(* com.duckpao.order.controller..*(..))")
    public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();

        String method = request.getMethod();
        String path = request.getRequestURI();

        String requestBody = "";
        if (joinPoint.getArgs().length > 0) {
            requestBody = objectMapper.writeValueAsString(joinPoint.getArgs());
        }

        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        String responseBody = objectMapper.writeValueAsString(result);

        int status = 200;
        if (result instanceof ResponseEntity<?> responseEntity) {
            status = responseEntity.getStatusCode().value();
            responseBody = objectMapper.writeValueAsString(responseEntity.getBody());
        }

        ApiLog apiLog = ApiLog.builder()
                .method(method)
                .path(path)
                .requestBody(requestBody)
                .responseBody(responseBody)
                .status(status)
                .executionTimeMs(executionTime)
                .createdAt(LocalDateTime.now())
                .build();

        apiLogRepository.save(apiLog);

        log.info("API {} {} took {} ms", method, path, executionTime);

        return result;
    }
}

