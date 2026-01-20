package com.duckpao.order.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Builder
@Getter
public class OrderResponse {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private String status;
}
