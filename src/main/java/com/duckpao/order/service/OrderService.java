package com.duckpao.order.service;

import com.duckpao.order.dto.request.CreateOrderRequest;
import com.duckpao.order.entity.Order;
import com.duckpao.order.entity.Product;
import com.duckpao.order.entity.User;
import com.duckpao.order.repository.OrderRepository;
import com.duckpao.order.repository.ProductRepository;
import com.duckpao.order.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository
    ) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Tạo đơn hàng mới
     */
    public Order createOrder(CreateOrderRequest request) {

        // 1️⃣ Kiểm tra user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Tạo order
        Order order = new Order(user);

        // 3️⃣ Duyệt từng item
        for (CreateOrderRequest.OrderItemRequest item : request.getItems()) {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // kiểm tra & trừ stock
            product.decreaseStock(item.getQuantity());

            // thêm item vào order
            order.addItem(product, item.getQuantity());
        }

        // 4️⃣ Tính tổng tiền
        order.calculateTotalAmount();

        // 5️⃣ Lưu order (cascade sẽ lưu OrderItem)
        return orderRepository.save(order);
    }
}
