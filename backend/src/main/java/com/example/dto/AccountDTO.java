package com.example.dto;

public class AccountDTO {
    private Long customerId;
    private String accountType;

    // Default constructor
    public AccountDTO() {
    }

    // Constructor with all fields
    public AccountDTO(Long customerId, String accountType) {
        this.customerId = customerId;
        this.accountType = accountType;
    }

    // Getters and Setters
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