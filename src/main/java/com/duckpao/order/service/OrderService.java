package com.duckpao.order.service;
import com.duckpao.order.dto.request.CreateOrderRequest;
import com.duckpao.order.model.*;
import com.duckpao.order.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.duckpao.order.common.*;
import com.duckpao.order.exception.*;
import com.duckpao.order.dto.request.OrderItemRequest;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final  OrderRepository orderRepository;
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

        User user = validateUser(request.getUserId());

        Order order = createEmptyOrder(user);

        BigDecimal totalAmount = processOrderItems(order, request);

        order.setTotalAmount(totalAmount);

        return order;
    }

    // ================= LOGIC 1 =================
    private User validateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("User is blocked");
        }
        return user;
    }

    // ================= LOGIC 2 =================
    private Order createEmptyOrder(User user) {
        Order order = new Order(user, BigDecimal.ZERO, OrderStatus.NEW);
        return orderRepository.save(order);
    }

    // ================= LOGIC 3 + 4 =================
    private BigDecimal processOrderItems(Order order, CreateOrderRequest request) {

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest item : request.getItems()) {

            Product product = validateProduct(item);

            BigDecimal itemTotal = calculateItemTotal(product, item.getQuantity());
            totalAmount = totalAmount.add(itemTotal);

            saveOrderItem(order, product, item, product.getPrice());

            decreaseProductStock(product, item.getQuantity());
        }

        return totalAmount;
    }

    // ================= PRODUCT VALIDATION =================
    private Product validateProduct(OrderItemRequest item) {

        Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new BusinessException("Product not found"));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new BusinessException("Product is not active: " + product.getName());
        }

        if (product.getStock() < item.getQuantity()) {
            throw new BusinessException("Not enough stock for product: " + product.getName());
        }

        return product;
    }

    // ================= CALCULATION =================
    private BigDecimal calculateItemTotal(Product product, int quantity) {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    // ================= SAVE ORDER ITEM =================
    private void saveOrderItem(Order order,
                               Product product,
                               OrderItemRequest item,
                               BigDecimal priceAtPurchase) {

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(item.getQuantity())
                .price(priceAtPurchase)
                .build();

        orderItemRepository.save(orderItem);
    }

    // ================= STOCK =================
    private void decreaseProductStock(Product product, int quantity) {
        int updated = productRepository.decreaseStock(product.getId(), quantity);

        if (updated == 0) {
            throw new BusinessException(
                    "Not enough stock for product: " + product.getName()
            );
        }
    }

    public  List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

}
