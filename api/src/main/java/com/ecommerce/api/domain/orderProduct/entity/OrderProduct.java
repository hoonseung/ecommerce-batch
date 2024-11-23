package com.ecommerce.api.domain.orderProduct.entity;

import com.ecommerce.api.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_products")
@Entity
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private String productId;
    private Integer itemPrice;
    private Integer quantity;

    public static OrderProduct createOrderProduct(Order order, String productId, Integer itemPrice, Integer quantity) {
        return new OrderProduct(
                null,
                order,
                productId,
                itemPrice,
                quantity
        );
    }
}
