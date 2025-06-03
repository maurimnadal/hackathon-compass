package com.example.controller;

import com.example.dto.AccountDTO;
import com.example.dto.TransactionDTO;
import com.example.model.Account;
import com.example.model.Customer;
import com.example.model.Transaction;
import com.example.repository.CustomerRepository;
import com.example.service.AccountService;
import com.example.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private TransactionService transactionService;
    
    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAccount_Success() throws Exception {
        AccountDTO dto = new AccountDTO(1L, "S");
        Account account = new Account();
        account.setId(10L);

        Mockito.when(accountService.createAccount(any())).thenReturn(account);

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createAccount_InvalidRequest() throws Exception {
        Mockito.when(accountService.createAccount(any())).thenThrow(new IllegalArgumentException("Invalid data"));

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AccountDTO())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAccountsByCustomerId_Success() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setBirthday(LocalDate.of(1990, 1, 1));
        
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Mockito.when(accountService.getAccountsByCustomerId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/accounts/customer/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAccountsByCustomerId_InvalidId() throws Exception {
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/customer/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deposit_Success() throws Exception {
        Transaction tx = new Transaction();
        tx.setId(100L);
        tx.setAmount(new BigDecimal("50.00"));

        Mockito.when(accountService.processDeposit(any())).thenReturn(tx);

        TransactionDTO dto = new TransactionDTO(null, new BigDecimal("50.00"));

        mockMvc.perform(post("/api/accounts/1/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void obterConta_Found() throws Exception {
        Account acc = new Account();
        acc.setId(1L);
        acc.setBalance(new BigDecimal("123.45"));
        acc.setAccountType(Account.AccountType.SAVINGS);

        Mockito.when(accountService.findById(1L)).thenReturn(Optional.of(acc));

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void obterConta_NotFound() throws Exception {
        Mockito.when(accountService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void withdraw_Success() throws Exception {
        Transaction tx = new Transaction();
        tx.setAmount(new BigDecimal("50.00"));
        tx.setAccount(new Account());
        tx.getAccount().setId(1L);
        tx.getAccount().setBalance(new BigDecimal("950.00"));

        Mockito.when(accountService.processWithdrawal(any())).thenReturn(tx);

        TransactionDTO dto = new TransactionDTO(null, new BigDecimal("50.00"));

        mockMvc.perform(post("/api/accounts/1/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void withdraw_MismatchedAccountId() throws Exception {
        TransactionDTO dto = new TransactionDTO(2L, new BigDecimal("100.00"));

        mockMvc.perform(post("/api/accounts/1/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void withdraw_InvalidAmount() throws Exception {
        Mockito.when(accountService.processWithdrawal(any())).thenThrow(new IllegalArgumentException("Insufficient funds"));

        TransactionDTO dto = new TransactionDTO(null, new BigDecimal("1000000.00"));

        mockMvc.perform(post("/api/accounts/1/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}