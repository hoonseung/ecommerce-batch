package com.ecommerce.batch.service.transaction;


import com.ecommerce.batch.domain.transaction.report.TransactionReport;
import com.ecommerce.batch.domain.transaction.report.TransactionReportMemoryRepository;
import com.ecommerce.batch.dto.transaction.log.TransactionLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TransactionReportAccumulator {


    public static final String TRANSACTION_SUCCESS = "SUCCESS";
    private final TransactionReportMemoryRepository repository;

    public void accumulate(TransactionLog transactionLog) {
        if (!TRANSACTION_SUCCESS.equals(transactionLog.transactionStatus())) {
            return;
        }
        repository.put(TransactionReport.from(transactionLog));
    }
}
