package com.ecommerce.api.domain.order.controller;

import com.ecommerce.api.domain.api.ApiResponse;
import com.ecommerce.api.domain.order.dto.OrderRequest;
import com.ecommerce.api.domain.order.dto.OrderResponse;
import com.ecommerce.api.domain.order.dto.OrderResult;
import com.ecommerce.api.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("")
    public ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResult orderResult = orderService.createOrder(
                orderRequest.customerId(),
                orderRequest.paymentMethod(),
                orderRequest.getOrderProductCommandList());

        return ApiResponse.success(OrderResponse.from(orderResult));
    }
}
