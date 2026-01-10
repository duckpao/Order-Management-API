package com.duckpao.order.dto.request;

import java.util.List;

public class CreateOrderRequest {

    private Long userId;
    private List<OrderItemRequest> items;

    public Long getUserId() {
        return userId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;

        public Long getProductId() {
            return productId;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }
}
