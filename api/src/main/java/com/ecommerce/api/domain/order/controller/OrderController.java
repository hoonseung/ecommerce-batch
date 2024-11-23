package com.ecommerce.api.domain.order.controller;

import com.ecommerce.api.domain.api.ApiResponse;
import com.ecommerce.api.domain.order.dto.OrderRequest;
import com.ecommerce.api.domain.order.dto.OrderResponse;
import com.ecommerce.api.domain.order.dto.OrderResult;
import com.ecommerce.api.domain.order.dto.PaymentRequest;
import com.ecommerce.api.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("")
    public ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResult orderResult = orderService.createOrder(orderRequest.customerId(), orderRequest.paymentMethod(), orderRequest.getOrderProductCommandList());

        return ApiResponse.success(OrderResponse.from(orderResult));
    }

    @PostMapping("/{orderId}/payment")
    public ApiResponse<OrderResponse> completePayment(@PathVariable("orderId") Long orderId, @RequestBody PaymentRequest paymentRequest) {
        OrderResult orderResult = orderService.completePayment(orderId, paymentRequest.success());
        return ApiResponse.success(OrderResponse.from(orderResult));
    }

    @PostMapping("/{orderId}/complete")
    public ApiResponse<OrderResponse> completeOrder(@PathVariable("orderId") Long orderId) {
        OrderResult orderResult = orderService.completeOrder(orderId);
        return ApiResponse.success(OrderResponse.from(orderResult));
    }

    @PostMapping("/{orderId}/cancel")
    public ApiResponse<OrderResponse> cancelOrder(@PathVariable("orderId") Long orderId) {
        OrderResult orderResult = orderService.cancelOrder(orderId);
        return ApiResponse.success(OrderResponse.from(orderResult));
    }
}
