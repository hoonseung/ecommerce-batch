package com.ecommerce.api.domain.payment;

import com.ecommerce.api.domain.payment.entity.Payment;
import com.ecommerce.api.domain.payment.exception.IllegalPaymentStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentTest {

    Payment payment;

    @BeforeEach
    void setup() {
        payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, null);
    }


    @Test
    void paymentPending() {
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void paymentComplete() {
        payment.complete();
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }

    @Test
    void paymentCompleteMustBePendingStatusOtherwiseThrowEx() {
        payment.complete();
        assertThatThrownBy(payment::complete)
                .isExactlyInstanceOf(IllegalPaymentStatusException.class);
    }

    @Test
    void paymentFailed() {
        payment.fail();
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
    }

    @Test
    void paymentFailedMustBePendingStatusOtherwiseThrowEx() {
        payment.complete();
        assertThatThrownBy(payment::fail)
                .isExactlyInstanceOf(IllegalPaymentStatusException.class);
    }

    @Test
    void paymentCancel() {
        payment.cancel();
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.CANCELED);
    }

    @Test
    void paymentCanceledMustBePendingStatus() {
        payment.complete();

        payment.cancel();

        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.REFUND);
    }

    @Test
    void paymentCancelMustBePendingAndFailStatusOtherwiseThrowEx() {
        payment.complete();
        payment.cancel();

        assertThatThrownBy(payment::cancel)
                .isExactlyInstanceOf(IllegalPaymentStatusException.class);
    }


}