package com.ecommerce.api.domain.order.service;

import com.ecommerce.api.domain.order.dto.OrderProductCommand;
import com.ecommerce.api.domain.order.dto.OrderResult;
import com.ecommerce.api.domain.order.entity.Order;
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
        Order orderPs = orderRepository.save(order);
        return OrderResult.from(orderPs);
    }
}
