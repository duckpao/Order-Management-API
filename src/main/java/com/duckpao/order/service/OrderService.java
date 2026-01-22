package com.duckpao.order.service;

import com.duckpao.order.adapter.OrderAdapter;
import com.duckpao.order.common.OrderStatus;
import com.duckpao.order.domain.OrderDomainService;
import com.duckpao.order.domain.ProductDomainService;
import com.duckpao.order.dto.request.CreateOrderRequest;
import com.duckpao.order.model.*;
import com.duckpao.order.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.duckpao.order.exception.*;
import com.duckpao.order.dto.request.OrderItemRequest;

import java.math.BigDecimal;
import java.util.List;

import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Transactional
@Log4j2
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderAdapter orderAdapter;
    private final OrderDomainService orderDomainService;
    private final ProductDomainService productDomainService;


    public Order createOrder(CreateOrderRequest request) {
        log.info("Creating order for userId={}", request.getUserId());

//Check user exist
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> {
            log.error("User not found for userId={}", request.getUserId());
            return BusinessException.badRequest("USER_NOT_FOUND", "User with id " + request.getUserId() + " not found");
        });
        //validate user through domain
        orderDomainService.validateUser(user);
        log.debug("User found: {}", user.getId());

        Order order = orderRepository.save(orderAdapter.toModel(user));
        log.info("Order created: {}", order.getId());
        BigDecimal totalAmount = request.getItems().stream().map(item -> processSingleItem(order, item)).reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);
        log.info("Order created successfully");
        return order;
    }

    public List<Order> getAllOrders() {
        log.info("Getting all orders");
        return orderRepository.findAll();
    }

    @Transactional
    public boolean deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        }

        if (order.getStatus() == OrderStatus.PAID) {
            throw  BusinessException.conflict("DELETE_IS_PAID", "Order with id " + orderId + " is paid");
        }

        orderItemRepository.deleteByOrder(order);
        orderRepository.delete(order);
        return true;
    }


    private BigDecimal processSingleItem(Order order, OrderItemRequest item) {
//Xu ly khi co 2 don hang dat cung luc
        Product product = productRepository.findByIdForUpdate(item.getProductId()).orElseThrow(() -> BusinessException.badRequest("PRODUCT_NOT_FOUND", "Product with id " + item.getProductId() + " not found"));
//Validate Product
        productDomainService.validateProduct(product, item.getQuantity());
        BigDecimal itemTotal = orderDomainService.calculateItemTotal(product, item.getQuantity());
        orderItemRepository.save(OrderItem.builder().order(order).product(product).quantity(item.getQuantity()).price(product.getPrice()).build());
//Decrease Stock
        orderDomainService.decreaseStock(product, item.getQuantity());
        return itemTotal;
    }


}
