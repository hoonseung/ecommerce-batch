package com.ecommerce.api.domain.order.entity;

import com.ecommerce.api.domain.order.OrderStatus;
import com.ecommerce.api.domain.order.exception.IllegalOrderStatusException;
import com.ecommerce.api.domain.orderProduct.entity.OrderProduct;
import com.ecommerce.api.domain.payment.PaymentMethod;
import com.ecommerce.api.domain.payment.PaymentStatus;
import com.ecommerce.api.domain.payment.entity.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private LocalDateTime orderDate;
    @Enumerated(STRING)
    private OrderStatus orderStatus;
    private Long customerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private List<OrderProduct> orderProducts;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;


    public static Order createOrder(Long customerId) {
        return new Order(null, LocalDateTime.now(), OrderStatus.PENDING_PAYMENT, customerId, new ArrayList<>(), null);
    }

    public void addOrderProduct(String productId, Integer itemPrice, Integer quantity) {
        OrderProduct orderProduct = OrderProduct.createOrderProduct(this, productId, itemPrice, quantity);
        orderProducts.add(orderProduct);
    }

    public void initPayment(PaymentMethod paymentMethod) {
        this.payment = Payment.createPayment(paymentMethod, totalAmount(), this);
    }

    public Integer totalAmount() {
        return orderProducts.stream().mapToInt(item -> item.getItemPrice() * item.getQuantity()).sum();
    }

    public void completePayment(boolean complete) {
        if (this.orderStatus != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalOrderStatusException("can only be completed while pending for payment");
        }

        if (complete) {
            payment.complete();
            this.orderStatus = OrderStatus.PROCESSING;
        } else {
            payment.fail();
        }
    }


    public PaymentStatus getPaymentStatus() {
        return payment.getPaymentStatus();
    }

    public boolean isPaymentComplete() {
        return payment.isPaymentComplete();
    }

    public Long productKindCount() {
        return (long) this.getOrderProducts().size();
    }

    public Long totalProductQuantity() {
        return this.getOrderProducts().stream().mapToLong(OrderProduct::getQuantity).sum();
    }

    public Long getPaymentId() {
        return this.payment.getPaymentId();
    }

    public PaymentMethod getPaymentMethod() {
        return this.payment.getPaymentMethod();
    }

    public LocalDateTime getPaymentDate() {
        return this.payment.getPaymentDate();
    }


    public void complete() {
        if (isPaymentComplete() && this.orderStatus == OrderStatus.PROCESSING) {
            this.orderStatus = OrderStatus.COMPLETED;
        } else {
            throw new IllegalOrderStatusException("can only be completed while processing for order");
        }
    }

    public void cancel() {
        if (this.orderStatus == OrderStatus.COMPLETED || this.orderStatus == OrderStatus.CANCELED) {
            throw new IllegalOrderStatusException("cannot be canceled in the completed or canceled status");
        }
        payment.cancel();
        this.orderStatus = OrderStatus.CANCELED;
    }
}
