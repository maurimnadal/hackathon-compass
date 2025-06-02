package com.exemplo.dto;

import java.math.BigDecimal;

public class AccountResponseDTO {
    private Long id;
    private String accountType;
    private BigDecimal balance;

    // Construtor padrão
    public AccountResponseDTO() {
    }

    // Construtor com todos os campos
    public AccountResponseDTO(Long id, String accountType, BigDecimal balance) {
        this.id = id;
        this.accountType = accountType;
        this.balance = balance;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}