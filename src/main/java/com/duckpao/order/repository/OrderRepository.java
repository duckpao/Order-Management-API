package com.duckpao.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.duckpao.order.model.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {

}
