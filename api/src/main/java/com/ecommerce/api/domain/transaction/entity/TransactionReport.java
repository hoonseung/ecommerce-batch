package com.ecommerce.api.domain.transaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "transaction_reports")
@IdClass(TransactionReportId.class)
@Entity
public class TransactionReport {

    @Id
    private LocalDate transactionDate;
    @Id
    private String transactionType;

    private Long transactionCount;
    private Long totalAmount;
    private Long customerCount;
    private Long orderCount;
    private Long paymentMethodKindCount;
    private BigDecimal avgProductCount;
    private Long totalProductQuantity;
}
