package com.duckpao.order.controller;

import com.duckpao.order.dto.request.CreateOrderRequest;
import com.duckpao.order.dto.response.OrderResponse;
import com.duckpao.order.exception.BusinessException;
import com.duckpao.order.exception.ErrorResponse;
import com.duckpao.order.model.Order;
import com.duckpao.order.service.OrderService;
import com.duckpao.order.adapter.OrderAdapter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderAdapter orderAdapter;

    //get /api/orders
    @GetMapping
    public ResponseEntity<?> getAllOrders(HttpServletRequest request) {
        List<OrderResponse> orders = orderService.getAllOrders()
                .stream()
                .map(orderAdapter::toResponse)
                .collect(Collectors.toList());

        if (orders.isEmpty()) {
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .errorCode("ORDER_NOT_FOUND")
                    .message("No orders found")
                    .path(request.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();
            log.error("Internal server error", error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        log.info("getAllOrders: {}", orders);
        return ResponseEntity.ok(orders);
    }

    // POST /api/orders
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request,
                                         HttpServletRequest httpRequest) {
        try {
            Order order = orderService.createOrder(request);
            log.info("createOrder successfully: {}", order);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(orderAdapter.toResponse(order));

        } catch (BusinessException ex) {
            // Lỗi nghiệp vụ: user blocked, product inactive, hết stock…
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.CONFLICT.value())
                    .errorCode("ORDER_CONFLICT")
                    .message(ex.getMessage())
                    .path(httpRequest.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();
            log.error("Order conflict", ex);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (Exception ex) {
            // Lỗi hệ thống thật sự
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

    // DELETE /api/orders/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id,
                                         HttpServletRequest httpRequest) {
        try {
            boolean deleted = orderService.deleteOrder(id);

            if (!deleted) {
                ErrorResponse error = ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .errorCode("ORDER_NOT_FOUND")
                        .message("Order not found with id: " + id)
                        .path(httpRequest.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build();
                log.error("Not found order", error);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
log.info("deleteOrder successfully: {}", id);
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "Order deleted successfully",
                    "orderId", id.toString()
            ));

        } catch (BusinessException ex) {
            // Ví dụ: order đã PAID thì không cho xóa
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.CONFLICT.value())
                    .errorCode("ORDER_CONFLICT")
                    .message(ex.getMessage())
                    .path(httpRequest.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();
            log.error("Order status conflict", ex);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }


}
