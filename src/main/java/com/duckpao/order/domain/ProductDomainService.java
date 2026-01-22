package com.duckpao.order.domain;

import com.duckpao.order.common.ProductStatus;
import com.duckpao.order.exception.BusinessException;
import com.duckpao.order.model.Product;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Log4j2
public class ProductDomainService {

    public void validateProduct(Product product, int quantity) {
//check status cua product
        if (product.getStatus() != ProductStatus.ACTIVE) {
            log.error("Product status is not ACTIVE");
            throw  BusinessException.badRequest("PRODUCT_NOT_ACTIVE","Product is not active with id " + product.getId());
        }
        if (product.getStock() < quantity) {
            log.error("Product stock is not enough");
            throw  BusinessException.conflict("PRODUCT_OUT_OF_STOCK","Product " + product.getId() +" is not available");
        }
        log.debug("Processing productId={}, quantity={}", product.getId(), product.getStock());
    }
}
