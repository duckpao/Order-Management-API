package com.duckpao.order.controller;

import com.duckpao.order.dto.request.CreateProductRequest;
import com.duckpao.order.dto.request.UpdateStockRequest;
import com.duckpao.order.dto.response.ProductResponse;
import com.duckpao.order.model.Product;
import com.duckpao.order.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ✅ POST /api/products
    @PostMapping
    public ProductResponse createProduct(@RequestBody CreateProductRequest request) {
        return productService.create(request);
    }

    // ✅ GET /api/products
    @GetMapping
    public List<ProductResponse> getProducts() {
        return productService.getAll();
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
