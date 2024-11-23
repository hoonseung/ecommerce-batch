package com.ecommerce.api.domain.order.repository;

import com.ecommerce.api.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
