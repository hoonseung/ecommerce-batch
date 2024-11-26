package com.ecommerce.api.domain.transaction.controller;

import com.ecommerce.api.domain.api.ApiResponse;
import com.ecommerce.api.domain.transaction.dto.TransactionReportResponses;
import com.ecommerce.api.domain.transaction.service.TransactionReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequestMapping("/v1/transaction/reports")
@RequiredArgsConstructor
@RestController
public class TransactionReportController {

    private final TransactionReportService transactionReportService;

    @GetMapping("")
    public ApiResponse<TransactionReportResponses> getTransactionReports(
            @RequestParam("dt")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        TransactionReportResponses responses = TransactionReportResponses.from(transactionReportService.findByDate(date));
        return ApiResponse.success(responses);
    }
}
