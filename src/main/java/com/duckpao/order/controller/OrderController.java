package com.duckpao.order.controller;

import com.duckpao.order.dto.request.CreateOrderRequest;
import com.duckpao.order.dto.response.OrderResponse;
import com.duckpao.order.model.Order;
import com.duckpao.order.service.OrderService;
import com.duckpao.order.adapter.OrderAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderAdapter orderAdapter;
//get /api/orders
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders()
                .stream()
                .map(orderAdapter::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }
//post /api/orders
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderAdapter.toResponse(order));
    }

    //delete /api/orders/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(Map.of(
                "message", "Order deleted successfully",
                "orderId", id.toString()
        ));
    }



}
