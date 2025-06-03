package com.example.dto;

import java.util.List;

public class CustomerAccountsDTO {
    private Long customerId;
    private String name;
    private String email;
    private String birthday;
    private List<AccountResponseDTO> accounts;

    public CustomerAccountsDTO() {
    }

    public CustomerAccountsDTO(Long customerId, String name, String email, String birthday, List<AccountResponseDTO> accounts) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.accounts = accounts;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public List<AccountResponseDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountResponseDTO> accounts) {
        this.accounts = accounts;
    }
}