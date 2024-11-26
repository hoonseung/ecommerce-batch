package com.ecommerce.api.domain.transaction.dto;

import java.util.List;

public record TransactionReportResponses(

        List<TransactionReportResponse> reportResponses

) {

    public static TransactionReportResponses from(TransactionReportResults results) {
        return new TransactionReportResponses(
                results.reportResults()
                        .stream()
                        .map(TransactionReportResponse::from)
                        .toList());
    }
}
