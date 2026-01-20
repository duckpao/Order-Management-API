package com.duckpao.order.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderRequest {

    private Long userId;
    private Long productId;
    private List<OrderItemRequest> items;

}
