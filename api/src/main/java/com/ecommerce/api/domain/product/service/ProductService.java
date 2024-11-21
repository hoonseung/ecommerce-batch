package com.ecommerce.api.domain.product.service;

import com.ecommerce.api.domain.product.dto.ProductResult;
import com.ecommerce.api.domain.product.entity.Product;
import com.ecommerce.api.domain.product.exeption.ProductNotFoundException;
import com.ecommerce.api.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResult findOneProduct(String productId) {
        return ProductResult.from(findByProduct(productId));
    }

    public Page<ProductResult> findAllProduct(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductResult::from);
    }

    @Transactional
    public void decreaseStockQuantity(String productId, int stockQuantity) {
        Product productPs = findByProduct(productId);
        productPs.decreaseStockQuantity(stockQuantity);
    }

    @Transactional
    public void increaseStockQuantity(String productId, int stockQuantity) {
        Product productPs = findByProduct(productId);
        productPs.increaseStockQuantity(stockQuantity);
    }

    private Product findByProduct(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
