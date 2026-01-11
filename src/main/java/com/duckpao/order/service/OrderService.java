package com.duckpao.order.service;

import com.duckpao.order.common.*;
import com.duckpao.order.dto.request.CreateOrderRequest;
import com.duckpao.order.entity.*;
import com.duckpao.order.exception.BusinessException;
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
                .orElseThrow(() -> new BusinessException("User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("User is blocked");
        }


        Order order = new Order(user, BigDecimal.ZERO, OrderStatus.NEW);
        orderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;


        for (CreateOrderRequest.OrderItemRequest item : request.getItems()) {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new BusinessException("Product not found"));

            // Product phải ACTIVE
            if (product.getStatus() != ProductStatus.ACTIVE) {
                throw new BusinessException("Product is not active: " + product.getName());
            }

            // Stock phải đủ
            if (product.getStock() < item.getQuantity()) {
                throw new BusinessException("Not enough stock for product: " + product.getName());
            }

            // Lấy giá tại thời điểm mua
            BigDecimal priceAtPurchase = product.getPrice();
            BigDecimal itemTotal =
                    priceAtPurchase.multiply(BigDecimal.valueOf(item.getQuantity()));

            totalAmount = totalAmount.add(itemTotal);

            // Tạo OrderItem
            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    item.getQuantity(),
                    priceAtPurchase
            );
            orderItemRepository.save(orderItem);

            // Trừ kho
            product.decreaseStock(item.getQuantity());
        }

        // ===== Logic 3: Set tổng tiền =====
        order.setTotalAmount(totalAmount);

        return order;
    }
}
