package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponseDTO {
    private Long accountId;
    private BigDecimal withdrawnAmount;
    private BigDecimal currentBalance;
    private LocalDateTime timestamp;

    // Construtor padrão
    public TransactionResponseDTO() {
    }

    // Construtor com todos os campos
    public TransactionResponseDTO(Long accountId, BigDecimal withdrawnAmount, BigDecimal currentBalance, LocalDateTime timestamp) {
        this.accountId = accountId;
        this.withdrawnAmount = withdrawnAmount;
        this.currentBalance = currentBalance;
        this.timestamp = timestamp;
    }

    // Getters e Setters
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getWithdrawnAmount() {
        return withdrawnAmount;
    }

    public void setWithdrawnAmount(BigDecimal withdrawnAmount) {
        this.withdrawnAmount = withdrawnAmount;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}