package com.duckpao.order.controller;

import com.duckpao.order.adapter.ProductAdapter;
import com.duckpao.order.dto.request.CreateProductRequest;
import com.duckpao.order.dto.request.UpdateStockRequest;
import com.duckpao.order.dto.response.ProductResponse;
import com.duckpao.order.exception.BusinessException;
import com.duckpao.order.exception.ErrorResponse;
import com.duckpao.order.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductAdapter productAdapter;

    // POST /api/products
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest request,
                                           HttpServletRequest httpRequest) {
        try {
            ProductResponse response = productService.create(request);
            log.info("Created product with id " + response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (BusinessException ex) {
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.CONFLICT.value())
                    .errorCode("PRODUCT_CONFLICT")
                    .message(ex.getMessage())
                    .path(httpRequest.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();
            log.error("Product creation failed", ex);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);

        } catch (Exception ex) {
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorCode("INTERNAL_ERROR")
                    .message("Unexpected server error")
                    .path(httpRequest.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();
            log.error("Internal server error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // GET /api/products
    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        List<ProductResponse> response = productService.getProducts().stream()
                .map(productAdapter::toResponse)
                .collect(Collectors.toList());

        if (response.isEmpty()) {
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .errorCode("PRODUCT_NOT_FOUND")
                    .message("No products found")
                    .path(request.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        return ResponseEntity.ok(response);
    }

    // PUT /api/products/{id}/stock
    @PutMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Long id,
                                         @RequestBody UpdateStockRequest request,
                                         HttpServletRequest httpRequest) {
        try {
            ProductResponse response = productService.updateStock(id, request);
            return ResponseEntity.ok(response);

        } catch (BusinessException ex) {
            // Ví dụ: product không tồn tại, product bị BLOCKED, stock âm…
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.CONFLICT.value())
                    .errorCode("STOCK_UPDATE_CONFLICT")
                    .message(ex.getMessage())
                    .path(httpRequest.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);

        } catch (Exception ex) {
            ErrorResponse error = ErrorResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorCode("INTERNAL_ERROR")
                    .message("Unexpected server error")
                    .path(httpRequest.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
