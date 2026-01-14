package com.duckpao.order.repository;

import com.duckpao.order.model.Order;
import com.duckpao.order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    public void deleteByOrder(Order order);
}
