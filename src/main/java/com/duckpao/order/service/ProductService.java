package com.duckpao.order.service;

import com.duckpao.order.adapter.ProductAdapter;
import com.duckpao.order.common.ProductStatus;
import com.duckpao.order.dto.request.CreateProductRequest;
import com.duckpao.order.dto.request.UpdateStockRequest;
import com.duckpao.order.dto.response.ProductResponse;
import com.duckpao.order.model.Product;
import com.duckpao.order.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // CREATE
    public ProductResponse create(CreateProductRequest request) {
        Product product = ProductAdapter.toModel(request);
        Product saved = productRepository.save(product);
        return ProductAdapter.toResponse(saved);
    }

    // GET ALL
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductAdapter::toResponse)
                .collect(Collectors.toList());
    }

    // UPDATE STOCK
    public ProductResponse updateStock(Long id, UpdateStockRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new RuntimeException("Product is not active");
        }

        if (request.getStock() < 0) {
            throw new RuntimeException("Stock cannot be negative");
        }


        product.setStock(request.getStock());

        return ProductAdapter.toResponse(product);
    }
}
