package com.duckpao.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.duckpao.order.model.Order;
import com.duckpao.order.common.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatusAndCreatedAtBefore(OrderStatus status, LocalDateTime time);

}
