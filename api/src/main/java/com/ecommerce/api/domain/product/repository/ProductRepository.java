package com.ecommerce.api.domain.product.repository;

import com.ecommerce.api.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

}
