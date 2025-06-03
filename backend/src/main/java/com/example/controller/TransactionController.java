package com.example.controller;

import com.example.dto.TransactionReportDTO;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/report/{customerId}")
    public ResponseEntity<?> getTransactionReport(
            @PathVariable Long customerId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            TransactionReportDTO report = transactionService.generateTransactionReport(customerId, startDate, endDate);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}