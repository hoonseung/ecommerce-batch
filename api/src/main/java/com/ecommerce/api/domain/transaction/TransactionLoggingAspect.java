package com.ecommerce.api.domain.transaction;

import com.ecommerce.api.domain.order.dto.OrderResult;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Aspect
public class TransactionLoggingAspect {

    private final TransactionService transactionService;

    @Pointcut("execution(* com.ecommerce.api.domain.order.service.OrderService.createOrder(..))")
    public void orderCreation() {
    }

    @AfterReturning(pointcut = "orderCreation()", returning = "newOrder")
    public void logOrderCreationSuccess(Object newOrder) {
        transactionService.logTransaction(
                TransactionType.ORDER_CREATION,
                TransactionStatus.SUCCESS,
                "order created!! you can processing payment",
                (OrderResult) newOrder);
    }

    @AfterThrowing(pointcut = "orderCreation()", throwing = "exception")
    public void logOrderCreationFailure(Exception exception) {
        transactionService.logTransaction(
                TransactionType.ORDER_CREATION,
                TransactionStatus.FAILURE,
                "exception occurred while order creating!! message: " + exception.getMessage(),
                null
        );
    }

    @Pointcut("execution(* com.ecommerce.api.domain.order.service.OrderService.completeOrder(..))")
    public void orderComplete() {
    }

    @AfterReturning(pointcut = "orderComplete()", returning = "completedOrder")
    public void logOrderCompletionSuccess(Object completedOrder) {
        transactionService.logTransaction(
                TransactionType.ORDER_COMPLETION,
                TransactionStatus.SUCCESS,
                "order completed success!!",
                (OrderResult) completedOrder);
    }

    @AfterThrowing(pointcut = "orderComplete()", throwing = "exception")
    public void logOrderCompletionFailure(Exception exception) {
        transactionService.logTransaction(
                TransactionType.ORDER_COMPLETION,
                TransactionStatus.FAILURE,
                "exception occurred while order complete!! message: " + exception.getMessage(),
                null
        );
    }

    @Pointcut("execution(* com.ecommerce.api.domain.order.service.OrderService.cancelOrder(..))")
    public void orderCancel() {
    }

    @AfterReturning(pointcut = "orderCancel()", returning = "cancelOrder")
    public void logOrderCancellationSuccess(Object cancelOrder) {
        transactionService.logTransaction(
                TransactionType.ORDER_CANCELLATION,
                TransactionStatus.SUCCESS,
                "order canceled success!!",
                (OrderResult) cancelOrder);
    }

    @AfterThrowing(pointcut = "orderCancel()", throwing = "exception")
    public void logOrderCancellationFailure(Exception exception) {
        transactionService.logTransaction(
                TransactionType.ORDER_CANCELLATION,
                TransactionStatus.FAILURE,
                "exception occurred while order cancel!! message: " + exception.getMessage(),
                null
        );
    }


    @Pointcut("execution(* com.ecommerce.api.domain.order.service.OrderService.completePayment(..))")
    public void paymentComplete() {
    }

    @AfterReturning(pointcut = "paymentComplete()", returning = "updateOrder")
    public void logOrderPaymentComplete(Object updateOrder) {
        OrderResult order = (OrderResult) updateOrder;
        if (order.paymentSuccess()) {
            transactionService.logTransaction(
                    TransactionType.PAYMENT_COMPLETION,
                    TransactionStatus.SUCCESS,
                    "order payment success!!",
                    (OrderResult) updateOrder
            );
        } else {
            transactionService.logTransaction(
                    TransactionType.PAYMENT_COMPLETION,
                    TransactionStatus.FAILURE,
                    "order payment failed!!",
                    (OrderResult) updateOrder
            );
        }
    }

    @AfterThrowing(pointcut = "paymentComplete()", throwing = "exception")
    public void logOrderPaymentFailure(Exception exception) {
        transactionService.logTransaction(
                TransactionType.PAYMENT_COMPLETION,
                TransactionStatus.FAILURE,
                "exception occurred while order payment!! message: " + exception.getMessage(),
                null
        );
    }
}
