package com.duckpao.order.repository;

import com.duckpao.order.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Có thể mở rộng sau
    // Optional<User> findByEmail(String email);
}
