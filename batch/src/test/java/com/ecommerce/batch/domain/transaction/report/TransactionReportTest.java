package com.ecommerce.batch.domain.transaction.report;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TransactionReportTest {


    @Test
    void testAdd() {
        TransactionReport report1 = TransactionReport.of(
                LocalDate.now(), "ORDER_CREATION", 1L,
                1000L, 1L, 1L,
                1L, BigDecimal.valueOf(1L), 100L,
                new HashSet<>(Set.of("1")), new HashSet<>(Set.of("1")), new HashSet<>(Set.of("DEBIT_CARD")),
                1L
        );

        TransactionReport report2 = TransactionReport.of(
                LocalDate.now(), "ORDER_CREATION", 1L, 100L, 1L, 1L,
                1L, BigDecimal.valueOf(1L), 100L,
                new HashSet<>(Set.of("2")), new HashSet<>(Set.of("2")), new HashSet<>(Set.of("CREDIT_CARD")),
                2L
        );

        report1.add(report2);


        assertAll(
                () -> assertThat(report1.getTransactionCount()).isEqualTo(2L),
                () -> assertThat(report1.getTotalAmount()).isEqualTo(1100L),

                () -> assertThat(report1.getCustomerCount()).isEqualTo(2L),
                () -> assertThat(report1.getOrderCount()).isEqualTo(2L),
                () -> assertThat(report1.getPaymentMethodKindCount()).isEqualTo(2L),

                () -> assertThat(report1.getSumProductCount()).isEqualTo(3L),
                () -> assertThat(report1.getAvgProductCount()).isEqualTo(BigDecimal.valueOf(1.5))
        );
    }

}