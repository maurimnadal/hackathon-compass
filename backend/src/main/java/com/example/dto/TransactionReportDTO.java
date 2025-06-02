package com.example.dto;

import java.time.LocalDate;
import java.util.List;

public class TransactionReportDTO {
    private long customerId;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<TransactionReportItemDTO> transactions;

    // Getters and setters
    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<TransactionReportItemDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionReportItemDTO> transactions) {
        this.transactions = transactions;
    }
}