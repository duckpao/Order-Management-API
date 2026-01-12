package com.duckpao.order.dto.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateProductRequest {
    private String name;
    private BigDecimal price;
    private Integer stock;
}
