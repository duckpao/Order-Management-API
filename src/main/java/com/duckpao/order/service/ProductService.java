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
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductAdapter productAdapter;

    // CREATE
    public ProductResponse create(CreateProductRequest request) {
        Product product = productAdapter.toModel(request);
        Product saved = productRepository.save(product);
        return productAdapter.toResponse(saved);
    }

    // GET ALL
  public List<Product> getProducts() {
        return productRepository.findAll();
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

        return productAdapter.toResponse(product);
    }
}
