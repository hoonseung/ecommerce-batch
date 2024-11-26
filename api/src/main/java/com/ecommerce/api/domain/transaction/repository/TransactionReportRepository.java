package com.ecommerce.api.domain.transaction.repository;

import com.ecommerce.api.domain.transaction.entity.TransactionReport;
import com.ecommerce.api.domain.transaction.entity.TransactionReportId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionReportRepository extends JpaRepository<TransactionReport, TransactionReportId> {

    List<TransactionReport> findAllByTransactionDate(LocalDate transactionDate);
}
