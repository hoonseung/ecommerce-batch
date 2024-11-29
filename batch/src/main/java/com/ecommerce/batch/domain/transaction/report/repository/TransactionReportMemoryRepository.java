package com.ecommerce.batch.domain.transaction.report.repository;

import com.ecommerce.batch.domain.transaction.report.entity.TransactionReport;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionReportMemoryRepository {

    private final Map<String, TransactionReport> reportMap = new ConcurrentHashMap<>();

    public void put(TransactionReport report) {
        String key = getKey(report);
        reportMap.compute(key, (k, r) -> {
            if (r == null) {
                return report;
            }
            r.add(report);
            return r;
        });
    }

    private static String getKey(TransactionReport report) {
        return report.getTransactionDate() + "|" + report.getTransactionType();
    }

    public Iterator<TransactionReport> getIterator() {
        return reportMap.values().iterator();
    }
}
