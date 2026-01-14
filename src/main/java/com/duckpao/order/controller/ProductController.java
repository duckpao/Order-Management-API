package com.duckpao.order.controller;

import com.duckpao.order.adapter.ProductAdapter;
import com.duckpao.order.dto.request.CreateProductRequest;
import com.duckpao.order.dto.request.UpdateStockRequest;
import com.duckpao.order.dto.response.ProductResponse;
import com.duckpao.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
                ProductResponse response = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ GET /api/products
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        List<ProductResponse> response = productService.getProducts().stream()
                .map(productAdapter::toResponse)
                .collect(Collectors.toList());
        return    ResponseEntity.ok(response);
    }

    // ✅ PUT /api/products/{id}/stock
    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStock(
            @PathVariable Long id,
            @RequestBody UpdateStockRequest request
    ) {
       ProductResponse response = productService.updateStock(id, request);
        return ResponseEntity.ok(response);
    }
}
