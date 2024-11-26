package com.ecommerce.api.domain.transaction.service;

import com.ecommerce.api.domain.transaction.dto.TransactionReportResults;
import com.ecommerce.api.domain.transaction.repository.TransactionReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class TransactionReportService {

    private final TransactionReportRepository transactionReportRepository;

    public TransactionReportResults findByDate(LocalDate date) {
        return TransactionReportResults.from(transactionReportRepository.findAllByTransactionDate(date));
    }
}
