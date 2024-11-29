package com.ecommerce.batch.domain.transaction.report.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionReportId implements Serializable {


    private LocalDate transactionDate;
    private String transactionType;
}
