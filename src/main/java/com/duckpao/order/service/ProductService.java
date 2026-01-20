package com.duckpao.order.service;

import com.duckpao.order.adapter.ProductAdapter;
import com.duckpao.order.common.ProductStatus;
import com.duckpao.order.dto.request.CreateProductRequest;
import com.duckpao.order.dto.request.UpdateStockRequest;
import com.duckpao.order.dto.response.ProductResponse;
import com.duckpao.order.exception.BusinessException;
import com.duckpao.order.model.Product;
import com.duckpao.order.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductAdapter productAdapter;

    // CREATE
    public ProductResponse create(CreateProductRequest request) {
        Product product = productAdapter.toModel(request);
        Product saved = productRepository.save(product);
        log.info("Saved product={}", saved);
        return productAdapter.toResponse(saved);
    }

    // GET ALL
    public List<Product> getProducts() {
        log.info("Retrieving all products");
        return productRepository.findAll();
    }

    // UPDATE STOCK
    public ProductResponse updateStock(Long id, UpdateStockRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        {
                            log.error("Product with id={} not found", id);
                            return BusinessException.notFound(
                                    "PRODUCT_NOT_FOUND",
                                    "Product with id " + id + " not found"
                            );
                        }
                );

        // Product phải ACTIVE
        if (product.getStatus() != ProductStatus.ACTIVE) {
            log.error("Product with id={} not active", id);
            throw BusinessException.badRequest(
                    "PRODUCT_NOT_AVAILABLE",
                    "Product with id " + id + " is not active"
            );
        }

        // Stock không được âm
        if (request.getStock() < 0) {
            log.error("Stock less than zero");
            throw BusinessException.badRequest(
                    "INVALID_STOCK",
                    "Stock cannot be negative"
            );
        }

        // Update stock
        product.setStock(request.getStock());

        // Save lại DB
        productRepository.save(product);
log.info("Updated stock for product={}", product);
        return productAdapter.toResponse(product);
    }

}
