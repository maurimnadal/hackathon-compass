package com.example.service;

import com.example.dto.TransactionReportDTO;
import com.example.dto.TransactionReportItemDTO;
import com.example.model.Customer;
import com.example.model.Transaction;
import com.example.repository.AccountRepository;
import com.example.repository.CustomerRepository;
import com.example.repository.TransactionRepository;
import com.example.util.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    public TransactionReportDTO generateTransactionReport(Long customerId, String startDateStr, String endDateStr) {
        // Validate start date
        DateValidator.validateDate(startDateStr);
        // Validate and parse dates
        LocalDate startDate = DateValidator.parseDate(startDateStr);
        LocalDate endDate = DateValidator.parseDate(endDateStr);

        // Validate date range
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("The end date cannot be earlier than the start date");
        }

        if(endDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("The end date cannot be in the future");
        }

        // Find customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // Find transactions
        List<Transaction> transactions = transactionRepository.findByCustomerIdAndDateRange(
                customerId,
                startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX)
        );

        // Create report
        TransactionReportDTO report = new TransactionReportDTO();
        report.setCustomerId(customerId);
        report.setCustomerName(customer.getName());
        report.setStartDate(startDate);
        report.setEndDate(endDate);

        // Map transactions to DTOs
        List<TransactionReportItemDTO> transactionDetails = transactions.stream()
                .map(this::mapToTransactionDetail)
                .collect(Collectors.toList());

        report.setTransactions(transactionDetails);
        return report;
    }

    private TransactionReportItemDTO mapToTransactionDetail(Transaction transaction) {
        TransactionReportItemDTO detail = new TransactionReportItemDTO();
        detail.setTransactionId(transaction.getId());
        detail.setType(transaction.getType().name());
        detail.setAmount(transaction.getAmount());
        detail.setDate(transaction.getTimestamp());
        return detail;
    }
}