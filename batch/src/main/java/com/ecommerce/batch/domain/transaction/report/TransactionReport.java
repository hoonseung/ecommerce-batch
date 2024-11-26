package com.ecommerce.batch.domain.transaction.report;

import com.ecommerce.batch.dto.transaction.log.TransactionLog;
import com.ecommerce.batch.util.DateUtils;
import jakarta.persistence.Transient;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionReport {
    // transactionDate + transactionType = key
    private LocalDate transactionDate;
    private String transactionType;

    private Long transactionCount;
    private Long totalAmount;
    private Long customerCount;
    private Long orderCount;
    private Long paymentMethodKindCount;
    private BigDecimal avgProductCount;
    private Long totalProductQuantity;

    @Transient
    private Set<String> checkingCustomerSet;
    @Transient
    private Set<String> checkingOrderSet;
    @Transient
    private Set<String> checkingPaymentMethodSet;
    @Transient
    private Long sumProductCount;

    public static TransactionReport from(TransactionLog log) {
        return new TransactionReport(
                DateUtils.parseToLocalDateTime(log.timestamp()).toLocalDate(),
                log.transactionType(),
                1L,
                Long.valueOf(log.getTotalAmount()),
                1L,
                1L,
                1L,
                BigDecimal.valueOf(Long.parseLong(log.getProductKindCount())),
                Long.valueOf(log.getTotalProductQuantity()),
                new HashSet<>(Set.of(log.getCustomerId())),
                new HashSet<>(Set.of(log.getOrderId())),
                new HashSet<>(Set.of(log.getPaymentMethod())),
                Long.valueOf(log.getProductKindCount())
        );
    }

    public void add(TransactionReport report) {
        transactionCount += report.getTransactionCount();
        totalAmount += report.getTotalAmount();

        checkingCustomerSet.addAll(report.getCheckingCustomerSet());
        customerCount = (long) checkingCustomerSet.size();

        checkingOrderSet.addAll(report.getCheckingOrderSet());
        orderCount = (long) checkingOrderSet.size();

        checkingPaymentMethodSet.addAll(report.getCheckingPaymentMethodSet());
        paymentMethodKindCount = (long) checkingPaymentMethodSet.size();

        sumProductCount += report.getSumProductCount();

        avgProductCount = BigDecimal.valueOf((double) sumProductCount / transactionCount);
        totalProductQuantity += report.getTotalProductQuantity();
    }
}
