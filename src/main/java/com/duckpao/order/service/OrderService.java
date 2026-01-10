package com.duckpao.order.service;

import com.duckpao.order.common.OrderStatus;
import com.duckpao.order.dto.request.CreateOrderRequest;
import com.duckpao.order.entity.*;
import com.duckpao.order.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(UserRepository userRepository,
                        ProductRepository productRepository,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Order createOrder(CreateOrderRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order(user, BigDecimal.ZERO, OrderStatus.NEW);
        orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderRequest.OrderItemRequest item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for product " + product.getName());
            }

            product.decreaseStock(item.getQuantity());

            BigDecimal itemTotal =
                    product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);

            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    item.getQuantity(),
                    product.getPrice()
            );

            orderItemRepository.save(orderItem);
        }

        order.setTotalAmount(total);
        return order;
    }
}
