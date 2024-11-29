package com.ecommerce.batch.domain.transaction.report.repository;

import com.ecommerce.batch.domain.transaction.report.entity.TransactionReport;
import com.ecommerce.batch.domain.transaction.report.entity.TransactionReportId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionReportRepository extends JpaRepository<TransactionReport, TransactionReportId> {

    List<TransactionReport> findAllByTransactionDate(LocalDate transactionDate);
}
