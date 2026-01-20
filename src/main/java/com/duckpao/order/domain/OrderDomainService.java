package com.duckpao.order.domain;

import com.duckpao.order.common.UserStatus;
import com.duckpao.order.exception.BusinessException;
import com.duckpao.order.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Log4j2
@Service
public class OrderDomainService {

    public void validateUser(User user) {
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw BusinessException.badRequest("USER_NOT_AVAILABLE", "User is not active with id " + user.getId());
        }
    }

    public BigDecimal calculateItemTotal(Product product, int quantity) {
        log.info("Calculating total of quantity={} for product={}", quantity, product);
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public void decreaseStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw  BusinessException.conflict("PRODUCT_OUT_OF_STOCK","Product id " +product.getId() + " is less than quantity");
        }
        product.setStock(product.getStock() - quantity);
    }

}

