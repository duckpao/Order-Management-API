package com.duckpao.order.repository;

import com.duckpao.order.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
