package com.example.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object para informações de transação
 */
public class TransactionDTO {
    private Long accountId;
    private BigDecimal amount;

    // Construtor padrão
    public TransactionDTO() {
    }

    // Construtor com todos os campos
    public TransactionDTO(Long accountId, BigDecimal amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    // Getters e Setters
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}