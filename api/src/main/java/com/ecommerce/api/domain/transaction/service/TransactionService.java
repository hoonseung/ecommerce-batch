package com.ecommerce.api.domain.transaction.service;

import com.ecommerce.api.domain.order.dto.OrderResult;
import com.ecommerce.api.domain.transaction.TransactionStatus;
import com.ecommerce.api.domain.transaction.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    public static final String NA = "N/A";
    protected static Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public void logTransaction(TransactionType transactionType, TransactionStatus transactionStatus,
                               String message, OrderResult orderResult) {
        try {
            putMdc(transactionType, transactionStatus, orderResult);
            log(transactionStatus, message);
        } finally {
            MDC.clear();
        }
    }

    private void putMdc(TransactionType transactionType, TransactionStatus transactionStatus, OrderResult orderResult) {
        Optional.ofNullable(orderResult)
                .ifPresentOrElse(this::putOrder, this::putNAOrder);
        putTransaction(transactionType, transactionStatus);
    }

    private void putTransaction(TransactionType transactionType, TransactionStatus transactionStatus) {
        MDC.put("transactionType", transactionType.name());
        MDC.put("transactionStatus", transactionStatus.name());
    }

    private void putOrder(OrderResult orderResult) {
        MDC.put("orderId", orderResult.orderId().toString());
        MDC.put("customerId", orderResult.customerId().toString());
        MDC.put("totalAmount", orderResult.totalAmount().toString());
        MDC.put("paymentMethod", orderResult.paymentMethod().toString());
        MDC.put("productKindCount", orderResult.productKindCount().toString());
        MDC.put("totalProductQuantity", orderResult.totalProductQuantity().toString());
    }

    private void putNAOrder() {
        MDC.put("orderId", NA);
        MDC.put("customerId", NA);
        MDC.put("totalAmount", NA);
        MDC.put("paymentMethod", NA);
        MDC.put("productKindCount", NA);
        MDC.put("totalProductQuantity", NA);
    }

    private void log(TransactionStatus transactionStatus, String message) {
        if (transactionStatus == TransactionStatus.SUCCESS) {
            logger.info(message);
        } else {
            logger.error(message);
        }
    }
}
