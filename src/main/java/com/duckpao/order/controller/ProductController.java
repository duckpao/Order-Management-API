package com.duckpao.order.controller;
import com.duckpao.order.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.duckpao.order.entity.Product;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 📌 GET /api/products
    @GetMapping
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    // 📌 POST /api/products
    @PostMapping
    public Product create(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // 📌 PUT /api/products/{id}/stock
    @PutMapping("/{id}/stock")
    public Product updateStock(
            @PathVariable Long id,
            @RequestParam int quantity
    ) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.decreaseStock(quantity);
        return productRepository.save(product);
    }
}
