package com.duckpao.order.controller;

import com.duckpao.order.adapter.UserAdapter;
import com.duckpao.order.dto.request.CreateUserRequest;
import com.duckpao.order.dto.response.UserResponse;
import com.duckpao.order.exception.BusinessException;
import com.duckpao.order.exception.ErrorResponse;
import com.duckpao.order.model.User;
import com.duckpao.order.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserAdapter userAdapter;

    // POST /api/users
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request,
                                        HttpServletRequest httpRequest) {
        try {
            UserResponse response = userService.create(request);
            log.info("User created: {}", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (BusinessException ex) {
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.CONFLICT.value())
                    .errorCode("USER_CONFLICT")
                    .message(ex.getMessage())
                    .path(httpRequest.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();
            log.error("User creation failed", ex);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);

        } catch (Exception ex) {
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorCode("INTERNAL_ERROR")
                    .message("Unexpected server error")
                    .path(httpRequest.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();
            log.error("Internal server error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    // GET /api/users
    @GetMapping
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        List<UserResponse> response = userService.getAllUsers()
                .stream()
                .map(userAdapter::toResponse)
                .collect(Collectors.toList());

        if (response.isEmpty()) {
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .errorCode("USER_NOT_FOUND")
                    .message("No users found")
                    .path(request.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();
            log.error("User not found", error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
log.info("Users found: {}", response);
        return ResponseEntity.ok(response);
    }


    // GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id,
                                     HttpServletRequest httpRequest) {
        try {
            User user = userService.getById(id);
            UserResponse response = userAdapter.toResponse(user);
            log.info("User found: {}", response);
            return ResponseEntity.ok(response);

        } catch (BusinessException ex) {
            // Ví dụ: user không tồn tại
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .errorCode("USER_NOT_FOUND")
                    .message(ex.getMessage())
                    .path(httpRequest.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();
            log.error("User not found", ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        } catch (Exception ex) {
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorCode("INTERNAL_ERROR")
                    .message("Unexpected server error")
                    .path(httpRequest.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();
            log.error("Internal server error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
