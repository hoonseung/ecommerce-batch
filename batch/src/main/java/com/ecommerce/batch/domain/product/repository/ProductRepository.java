package com.ecommerce.batch.domain.product.repository;

import com.ecommerce.batch.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT p.productId FROM Product p")
    List<String> findAllProductIds();
}
