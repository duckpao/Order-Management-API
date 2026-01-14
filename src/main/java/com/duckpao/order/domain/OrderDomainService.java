package com.duckpao.order.domain;

import com.duckpao.order.common.ProductStatus;
import com.duckpao.order.common.UserStatus;
import com.duckpao.order.exception.BusinessException;
import com.duckpao.order.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class OrderDomainService {

    public void validateUser(User user) {
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("User is blocked");
        }
    }


    public void validateProduct(Product product, int quantity) {
//check status cua product
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new BusinessException("Product is not active: " + product.getName());
        }
        if (product.getStock() < quantity) {
            throw new BusinessException("Not enough stock for product: " + product.getName());
        }

    }

    public BigDecimal calculateItemTotal(Product product, int quantity) {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public void decreaseStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw new BusinessException("Not enough stock for product: " + product.getName());
        }
        product.setStock(product.getStock() - quantity);
    }

}

