package com.duckpao.order.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderRequest {

    private Long userId;
    private List<OrderItemRequest> items;

    public Long getUserId() {
        return userId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }


}
