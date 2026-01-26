package com.duckpao.order.aop;

import com.duckpao.order.model.ApiLog;
import com.duckpao.order.repository.ApiLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tools.jackson.databind.ObjectMapper;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect {

    private final ApiLogRepository apiLogRepository;
    private final ObjectMapper objectMapper;

    @Around("execution(* com.duckpao.order.controller..*(..))")
    public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String uri = request.getRequestURI();
        String method = request.getMethod();

        // log request info
        log.info("API CALL: {} {}", method, uri);

        // log body an toàn (chỉ serialize DTO)
        String requestBody = null;
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)) {
                try {
                    requestBody = objectMapper.writeValueAsString(arg);
                    log.info("Request body: {}", new ObjectMapper().writeValueAsString(arg));
                } catch (Exception e) {
                    log.warn("Cannot serialize request body");
                }
            }
        }

        Object result = null;
        String responseBody = null;
        int status = 200;

        try {
            result = joinPoint.proceed();
            if (result instanceof org.springframework.http.ResponseEntity<?> responseEntity) {
                status = responseEntity.getStatusCode().value();
                try {
                    responseBody = objectMapper.writeValueAsString(result);

                } catch (Exception e) {
                    responseBody = "Cannot serialize response body";
                }

            } else {
                status = 200;
                try {
                    requestBody = objectMapper.writeValueAsString(result);
                } catch (Exception e) {

                    responseBody = "Cannot serialize response body";
                }
            }
            return result;
        } catch (Exception e) {
            status = 500;
            responseBody = e.getMessage();
            throw e;
        } finally {
            ApiLog apiLog = ApiLog.
                    builder()
                    .method(method)
                    .path(uri)
                    .requestBody(requestBody)
                    .responseBody(responseBody)
                    .status(status)
                    .createdAt(java.time.LocalDateTime.now())
                    .build();
            apiLogRepository.save(apiLog);
            log.info("API LOG: {}", apiLog);
        }

    }

}

