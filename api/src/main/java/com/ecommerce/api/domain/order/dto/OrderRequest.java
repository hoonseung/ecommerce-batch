package com.ecommerce.api.domain.order.dto;

import com.ecommerce.api.domain.orderProduct.dto.OrderProductRequest;
import com.ecommerce.api.domain.payment.PaymentMethod;

import java.util.List;

public record OrderRequest(
        Long customerId,
        PaymentMethod paymentMethod,
        List<OrderProductRequest> orderProducts
) {

    public List<OrderProductCommand> getOrderProductCommandList() {
        return orderProducts.stream().map(OrderProductCommand::from).toList();
    }
}
