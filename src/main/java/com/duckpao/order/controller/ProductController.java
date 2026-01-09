package com.duckpao.order.controller;

import com.duckpao.order.dto.request.CreateProductRequest;
import com.duckpao.order.dto.request.UpdateStockRequest;
import com.duckpao.order.entity.Product;
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
    public Product createProduct(@RequestBody CreateProductRequest request) {
        return productService.create(request);
    }

    // ✅ GET /api/products
    @GetMapping
    public List<Product> getProducts() {
        return productService.getAll();
    }

    // ✅ PUT /api/products/{id}/stock
    @PutMapping("/{id}/stock")
    public Product updateStock(
            @PathVariable Long id,
            @RequestBody UpdateStockRequest request
    ) {
        return productService.updateStock(id, request);
    }
}
