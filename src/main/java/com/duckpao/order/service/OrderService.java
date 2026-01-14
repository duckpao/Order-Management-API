package com.duckpao.order.service;

import com.duckpao.order.adapter.OrderAdapter;
import com.duckpao.order.domain.OrderDomainService;
import com.duckpao.order.dto.request.CreateOrderRequest;
import com.duckpao.order.model.*;
import com.duckpao.order.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.duckpao.order.common.*;
import com.duckpao.order.exception.*;
import com.duckpao.order.dto.request.OrderItemRequest;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderAdapter orderAdapter;
    private final OrderDomainService orderDomainService;


    public Order createOrder(CreateOrderRequest request) {
//Check user exist
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new BusinessException("User not found"));
        //validate user through domain
        orderDomainService.validateUser(user);

        Order order = orderRepository.save(Order.builder().user(user).status(OrderStatus.NEW).totalAmount(BigDecimal.ZERO).build());

        BigDecimal totalAmount = request.getItems().stream().map(item -> processSingleItem(order, item)).reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);
        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new BusinessException("Order not found"));
        orderItemRepository.deleteByOrder(order);
        orderRepository.delete(order);

    }

    private BigDecimal processSingleItem(Order order, OrderItemRequest item) {
//Xu ly khi co 2 don hang dat cung luc
        Product product = productRepository.findByIdForUpdate(item.getProductId()).orElseThrow(() -> new BusinessException("Product not found"));
//Validate Product
        orderDomainService.validateProduct(product, item.getQuantity());

        BigDecimal itemTotal = orderDomainService.calculateItemTotal(product, item.getQuantity());

        orderItemRepository.save(OrderItem.builder().order(order).product(product).quantity(item.getQuantity()).price(product.getPrice()).build());
//Decrease Stock
        orderDomainService.decreaseStock(product, item.getQuantity());

        return itemTotal;
    }


}
