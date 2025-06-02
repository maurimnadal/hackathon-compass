package com.example.dto;

/**
 * Data Transfer Object para informações de conta
 */
public class AccountDTO {
    private Long customerId;
    private String accountType; // "S" para Savings (Poupança) ou "C" para Checking (Corrente)

    // Construtor padrão
    public AccountDTO() {
    }

    // Construtor com todos os campos
    public AccountDTO(Long customerId, String accountType) {
        this.customerId = customerId;
        this.accountType = accountType;
    }

    // Getters e Setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}