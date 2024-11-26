package com.ecommerce.api.domain.transaction.dto;

import com.ecommerce.api.domain.transaction.entity.TransactionReport;

import java.util.List;

public record TransactionReportResults(

        List<TransactionReportResult> reportResults

) {

    public static TransactionReportResults from(List<TransactionReport> reports) {
        return new TransactionReportResults(reports.stream()
                .map(TransactionReportResult::from)
                .toList());
    }
}
