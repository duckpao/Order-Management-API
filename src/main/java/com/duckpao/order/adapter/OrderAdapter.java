package com.duckpao.order.adapter;
import com.duckpao.order.common.OrderStatus;
import com.duckpao.order.dto.response.OrderResponse;
import com.duckpao.order.model.Order;
import com.duckpao.order.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderAdapter {
    public Order toModel(User user) {
        return Order.builder()
                .user(user)
                .totalAmount(BigDecimal.ZERO)
                .status(OrderStatus.NEW)
                .build();
    }

    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .userId(order.getUser().getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .build();
    }

}