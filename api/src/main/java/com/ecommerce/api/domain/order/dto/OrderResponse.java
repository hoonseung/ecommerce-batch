package com.ecommerce.api.domain.order.dto;

import com.ecommerce.api.domain.order.OrderStatus;
import com.ecommerce.api.domain.orderProduct.dto.OrderProductResponse;
import com.ecommerce.api.domain.payment.PaymentMethod;
import com.ecommerce.api.domain.payment.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long orderId,
        LocalDateTime orderDate,
        Long customerId,
        OrderStatus orderStatus,
        List<OrderProductResponse> orderProducts,

        Long productKindCount,
        Long totalProductQuantity,

        Long paymentId,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        LocalDateTime paymentDate,
        Integer totalAmount,
        boolean paymentSuccess) {

    public static OrderResponse from(OrderResult orderResult) {
        return new OrderResponse(
                orderResult.orderId(),
                orderResult.orderDate(),
                orderResult.customerId(),
                orderResult.orderStatus(),
                orderResult.orderProducts().stream().map(OrderProductResponse::from).toList(),
                orderResult.productKindCount(),
                orderResult.totalProductQuantity(),
                orderResult.paymentId(),
                orderResult.paymentMethod(),
                orderResult.paymentStatus(),
                orderResult.paymentDate(),
                orderResult.totalAmount(),
                orderResult.paymentSuccess()
        );
    }
}
