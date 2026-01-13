package com.duckpao.order.controller;

import com.duckpao.order.adapter.ProductAdapter;
import com.duckpao.order.dto.request.CreateProductRequest;
import com.duckpao.order.dto.request.UpdateStockRequest;
import com.duckpao.order.dto.response.OrderResponse;
import com.duckpao.order.dto.response.ProductResponse;
import com.duckpao.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductAdapter productAdapter;


    // ✅ POST /api/products
    @PostMapping
    public ProductResponse createProduct(@RequestBody CreateProductRequest request) {
        return productService.create(request);
    }

    // ✅ GET /api/products
    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.getProducts()
                .stream()
                .map(productAdapter::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ PUT /api/products/{id}/stock
    @PutMapping("/{id}/stock")
    public ProductResponse updateStock(
            @PathVariable Long id,
            @RequestBody UpdateStockRequest request
    ) {
        return productService.updateStock(id, request);
    }
}
