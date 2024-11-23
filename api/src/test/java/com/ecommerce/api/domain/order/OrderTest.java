package com.ecommerce.api.domain.order;

import com.ecommerce.api.domain.order.entity.Order;
import com.ecommerce.api.domain.order.exception.IllegalOrderStatusException;
import com.ecommerce.api.domain.payment.PaymentMethod;
import com.ecommerce.api.domain.payment.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTest {

    Order order;

    @BeforeEach
    void setup() {
        order = Order.createOrder(1L);
        order.addOrderProduct("product01", 1000, 5);
        order.initPayment(PaymentMethod.DEBIT_CARD);
    }

    @Test
    void orderComplete_thenChangeToPaymentStatusComplete() {
        order.completePayment(true);
        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PROCESSING),
                () -> assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED),
                () -> assertThat(order.isPaymentComplete()).isTrue()
        );
    }

    @Test
    void orderPaymentFail() {
        order.completePayment(false);
        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PENDING_PAYMENT),
                () -> assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.FAILED),
                () -> assertThat(order.isPaymentComplete()).isFalse()
        );
    }

    @Test
    void orderPaymentCompleteMustBeOnOrderStatusPendingPaymentOtherWiseThrowEx() {
        order.completePayment(true);
        assertThatThrownBy(() -> order.completePayment(true))
                .isExactlyInstanceOf(IllegalOrderStatusException.class);
    }

    @Test
    void orderComplete() {
        order.completePayment(true);

        order.complete();

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    void ifOrderCompleteWhenPaymentStatusFailThenThrowEx() {
        order.completePayment(false);

        assertThatThrownBy(() -> order.complete())
                .isExactlyInstanceOf(IllegalOrderStatusException.class);
    }

    @Test
    void canOrderCompleteMustBePaymentStatusCompletedAndOrderStatusProcessingElseThrowEx() {
        order.completePayment(false);

        assertThatThrownBy(() -> order.complete())
                .isExactlyInstanceOf(IllegalOrderStatusException.class);
    }

    @Test
    void orderCancel() {
        order.cancel();

        assertAll(
                () -> assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.CANCELED),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED)
        );
    }

    @Test
    void orderCancelWhenPaymentStatusComplete() {
        order.completePayment(true);
        order.cancel();

        assertAll(
                () -> assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.REFUND),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED)
        );
    }

    @Test
    void orderCancelWhenOrderStatusCompleteThenThrowEx() {
        order.completePayment(true);
        order.complete();

        assertThatThrownBy(() -> order.cancel())
                .isExactlyInstanceOf(IllegalOrderStatusException.class);
    }

}