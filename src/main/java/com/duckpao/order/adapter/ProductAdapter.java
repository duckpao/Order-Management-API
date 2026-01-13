package com.duckpao.order.adapter;

import com.duckpao.order.common.ProductStatus;
import com.duckpao.order.dto.request.CreateProductRequest;
import com.duckpao.order.dto.response.ProductResponse;
import com.duckpao.order.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductAdapter {

    // DTO -> Model
    public Product toModel(CreateProductRequest dto) {
        return Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .status(ProductStatus.ACTIVE)
                .build();
    }

    // Model -> DTO
    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus().name())
                .build();
    }
}
