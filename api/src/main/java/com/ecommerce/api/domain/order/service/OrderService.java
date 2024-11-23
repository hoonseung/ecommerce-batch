package com.ecommerce.api.domain.order.service;

import com.ecommerce.api.domain.order.dto.OrderProductCommand;
import com.ecommerce.api.domain.order.dto.OrderResult;
import com.ecommerce.api.domain.order.entity.Order;
import com.ecommerce.api.domain.order.exception.OrderNotFoundException;
import com.ecommerce.api.domain.order.repository.OrderRepository;
import com.ecommerce.api.domain.payment.PaymentMethod;
import com.ecommerce.api.domain.product.dto.ProductResult;
import com.ecommerce.api.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional
    public OrderResult createOrder(Long customerId, PaymentMethod paymentMethod, List<OrderProductCommand> orderProducts) {
        Order order = Order.createOrder(customerId);
        orderProducts.
                forEach(item -> {
                            ProductResult product = productService.findOneProduct(item.productId());
                            order.addOrderProduct(product.productId(), product.salesPrice(), item.quantity());
                        }
                );
        order.initPayment(paymentMethod);
        return getOrderResult(order);
    }


    @Transactional
    public OrderResult completePayment(Long orderId, boolean isSuccess) {
        Order orderPs = getOrder(orderId);
        orderPs.completePayment(isSuccess);
        decreaseProductStockQuantity(isSuccess, orderPs);
        return getOrderResult(orderPs);
    }

    @Transactional
    public OrderResult completeOrder(Long orderId) {
        Order orderPs = getOrder(orderId);
        orderPs.complete();
        return getOrderResult(orderPs);
    }

    @Transactional
    public OrderResult cancelOrder(Long orderId) {
        Order orderPs = getOrder(orderId);
        orderPs.cancel();
        increaseProductStockQuantity(orderPs);
        return getOrderResult(orderPs);
    }

    private void increaseProductStockQuantity(Order orderPs) {
        orderPs.getOrderProducts()
                .forEach(orderProduct ->
                        productService.increaseStockQuantity(orderProduct.getProductId(), orderProduct.getQuantity()));
    }

    private void decreaseProductStockQuantity(boolean isSuccess, Order orderPs) {
        if (isSuccess) {
            orderPs.getOrderProducts()
                    .forEach(orderProduct ->
                            productService.decreaseStockQuantity(orderProduct.getProductId(), orderProduct.getQuantity()));
        }
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private OrderResult getOrderResult(Order order) {
        Order orderPs = orderRepository.save(order);
        return OrderResult.from(orderPs);
    }
}
