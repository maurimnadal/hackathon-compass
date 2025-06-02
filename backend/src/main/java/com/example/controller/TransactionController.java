package com.example.controller;

import com.example.dto.TransactionReportDTO;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/report/{customerId}")
    public ResponseEntity<TransactionReportDTO> getTransactionReport(
            @PathVariable Long customerId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        TransactionReportDTO report = transactionService.generateTransactionReport(customerId, startDate, endDate);
        return ResponseEntity.ok(report);
    }
}