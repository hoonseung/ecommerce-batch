package com.ecommerce.batch.service.product;

import com.ecommerce.batch.domain.product.entity.Product;
import com.ecommerce.batch.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {


    private final ProductRepository productRepository;


    public Long getCountsForProduct() {
        return productRepository.count();
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public List<String> getProductIds() {
        return productRepository.findAllProductIds();
    }
}
