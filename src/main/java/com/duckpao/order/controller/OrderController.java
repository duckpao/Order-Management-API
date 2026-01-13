package com.duckpao.order.controller;

import com.duckpao.order.dto.request.CreateOrderRequest;
import com.duckpao.order.dto.response.OrderResponse;
import com.duckpao.order.service.OrderService;
import com.duckpao.order.adapter.OrderAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
private final OrderAdapter orderAdapter;

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return   orderService.getAllOrders()
                .stream()
                .map(orderAdapter::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }



}
