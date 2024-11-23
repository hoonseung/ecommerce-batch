package com.ecommerce.api.domain.order.dto;

import com.ecommerce.api.domain.order.OrderStatus;
import com.ecommerce.api.domain.order.entity.Order;
import com.ecommerce.api.domain.orderProduct.dto.OrderProductResult;
import com.ecommerce.api.domain.payment.PaymentMethod;
import com.ecommerce.api.domain.payment.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResult(
        Long orderId,
        LocalDateTime orderDate,
        Long customerId,
        OrderStatus orderStatus,
        List<OrderProductResult> orderProducts,

        Long productKindCount,
        Long totalProductQuantity,

        Long paymentId,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        LocalDateTime paymentDate,
        Integer totalAmount,
        boolean paymentSuccess) {

    public static OrderResult from(Order order) {
        return new OrderResult(
                order.getOrderId(),
                order.getOrderDate(),
                order.getCustomerId(),
                order.getOrderStatus(),
                order.getOrderProducts().stream().map(OrderProductResult::from).toList(),
                order.productKindCount(),
                order.totalProductQuantity(),
                order.getPaymentId(),
                order.getPaymentMethod(),
                order.getPaymentStatus(),
                order.getPaymentDate(),
                order.totalAmount(),
                order.isPaymentComplete()
        );
    }
}
