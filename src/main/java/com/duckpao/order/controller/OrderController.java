package com.duckpao.order.controller;

import com.duckpao.order.dto.request.CreateOrderRequest;
import com.duckpao.order.dto.response.OrderResponse;
import com.duckpao.order.model.Order;
import com.duckpao.order.service.OrderService;
import com.duckpao.order.adapter.OrderAdapter;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
     return   orderService.getAllOrders()
                .stream()
                .map(OrderAdapter::toResponse)
                .collect(Collectors.toList());
    }

}
