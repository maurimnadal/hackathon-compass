package com.example.model;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @SequenceGenerator(
        name = "account_seq",
        sequenceName = "account_seq",
        initialValue = 100001,
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "account_seq"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    // Enum para tipos de conta
    public enum AccountType {
        SAVINGS, // Poupança
        CHECKING // Corrente
    }

    // Construtor padrão
    public Account() {
    }

    // Construtor com customer e accountType
    public Account(Customer customer, AccountType accountType) {
        this.customer = customer;
        this.accountType = accountType;
        this.balance = BigDecimal.ZERO;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}