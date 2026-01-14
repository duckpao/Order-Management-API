package com.duckpao.order.exception;


import com.duckpao.order.common.ProductStatus;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class ErrorResponse {
    private int status;
    private String message;
    private String path;




}
