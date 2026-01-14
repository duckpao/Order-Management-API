package com.duckpao.order.repository;

import com.duckpao.order.model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {


    @Modifying
    @Query("""
        UPDATE Product p 
        SET p.stock = p.stock - :quantity 
        WHERE p.id = :productId 
          AND p.stock >= :quantity
    """)
    int decreaseStock(@Param("productId") Long productId,
                      @Param("quantity") int quantity);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);
}
