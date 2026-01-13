package com.duckpao.order.dto.response;

import com.duckpao.order.common.OrderStatus;
import com.duckpao.order.model.User;
import lombok.AccessLevel;
import lombok.*;

import java.math.BigDecimal;
@Builder
@Getter
public class OrderResponse {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private String status;
}
