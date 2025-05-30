package com.example.dto;

public class AccountDTO {
    private Long customerId;
    private String accountType; // "S" for Savings, "C" for Checking

    public AccountDTO() {
    }

    public AccountDTO(Long customerId, String accountType) {
        this.customerId = customerId;
        this.accountType = accountType;
    }

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