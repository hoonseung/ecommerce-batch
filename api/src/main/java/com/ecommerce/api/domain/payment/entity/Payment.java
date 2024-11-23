package com.ecommerce.api.domain.payment.entity;

import com.ecommerce.api.domain.order.entity.Order;
import com.ecommerce.api.domain.payment.PaymentMethod;
import com.ecommerce.api.domain.payment.PaymentStatus;
import com.ecommerce.api.domain.payment.exception.IllegalPaymentStatusException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payments")
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @Enumerated(STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(STRING)
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private Integer amount;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;


    public static Payment createPayment(PaymentMethod paymentMethod, Integer amount, Order order) {
        return new Payment(null, paymentMethod, PaymentStatus.PENDING, LocalDateTime.now(), amount, order);
    }


    public void complete() {
        if (this.paymentStatus != PaymentStatus.PENDING) {
            throw new IllegalPaymentStatusException("can only be completed while pending for payment");
        }
        this.paymentStatus = PaymentStatus.COMPLETED;
    }

    public void fail() {
        if (this.paymentStatus != PaymentStatus.PENDING) {
            throw new IllegalPaymentStatusException("can only be failed while pending for payment");
        }
        this.paymentStatus = PaymentStatus.FAILED;
    }

    public void cancel() {
        switch (this.paymentStatus) {
            case COMPLETED -> this.paymentStatus = PaymentStatus.REFUND;
            case FAILED, PENDING -> this.paymentStatus = PaymentStatus.CANCELED;
            case REFUND -> throw new IllegalPaymentStatusException("can not be canceled on refund status");
            case CANCELED -> throw new IllegalPaymentStatusException("already canceled");
        }
    }

    public boolean isPaymentComplete() {
        return this.paymentStatus == PaymentStatus.COMPLETED;
    }
}
