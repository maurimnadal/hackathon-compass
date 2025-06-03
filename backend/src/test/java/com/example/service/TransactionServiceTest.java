package com.example.service;

import com.example.dto.TransactionReportDTO;
import com.example.model.Account;
import com.example.model.Customer;
import com.example.model.Transaction;
import com.example.model.Transaction.TransactionType;
import com.example.repository.CustomerRepository;
import com.example.repository.TransactionRepository;
import com.example.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Customer customer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    void testGenerateTransactionReport_InvalidDateRange_ThrowsException() {
        final String start = "20240102";
        final String end = "20240101";

        assertThrows(IllegalArgumentException.class, () ->
                transactionService.generateTransactionReport(1L, start, end));
    }

    @Test
    void testGenerateTransactionReport_InvalidDateFormat_ThrowsException() {
        final String start = "2024-01-01";
        final String end = "2024-01-02";

        assertThrows(IllegalArgumentException.class, () ->
                transactionService.generateTransactionReport(1L, start, end));
    }

    @Test
    void testGenerateTransactionReport_CustomerNotFound_ThrowsException() {
        final String start = "20240101";
        final String end = "20240102";

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                transactionService.generateTransactionReport(1L, start, end));
    }

    @Test
    void testGenerateTransactionReport_NoTransactions_ReturnsEmptyList() {
        final String start = "20240101";
        final String end = "20240102";

        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 2);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerIdAndDateRange(
                eq(1L), 
                eq(startDate.atStartOfDay()), 
                eq(endDate.atTime(LocalTime.MAX))))
                .thenReturn(Collections.emptyList());

        TransactionReportDTO result = transactionService.generateTransactionReport(1L, start, end);

        assertNotNull(result);
        assertEquals(0, result.getTransactions().size());
        assertEquals("John Doe", result.getCustomerName());
        assertEquals(1L, result.getCustomerId());
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDate, result.getEndDate());
    }

    @Test
    void testGenerateTransactionReport_WithTransactions_ReturnsReport() {
        final String start = "20240101";
        final String end = "20240103";

        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 3);

        Transaction tx1 = new Transaction();
        tx1.setId(100L);
        tx1.setType(TransactionType.DEPOSIT);
        tx1.setAmount(new BigDecimal("100.00"));
        tx1.setTimestamp(LocalDateTime.of(2024, 1, 2, 10, 0));
        
        Account account1 = new Account();
        account1.setId(1L);
        tx1.setAccount(account1);

        Transaction tx2 = new Transaction();
        tx2.setId(101L);
        tx2.setType(TransactionType.WITHDRAWAL);
        tx2.setAmount(new BigDecimal("50.00"));
        tx2.setTimestamp(LocalDateTime.of(2024, 1, 2, 12, 0));
        
        Account account2 = new Account();
        account2.setId(2L);
        tx2.setAccount(account2);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerIdAndDateRange(
                eq(1L), 
                eq(startDate.atStartOfDay()), 
                eq(endDate.atTime(LocalTime.MAX))))
                .thenReturn(Arrays.asList(tx1, tx2));

        TransactionReportDTO result = transactionService.generateTransactionReport(1L, start, end);

        assertNotNull(result);
        assertEquals(2, result.getTransactions().size());
        assertEquals("John Doe", result.getCustomerName());
        assertEquals(100L, result.getTransactions().get(0).getTransactionId());
        assertEquals("DEPOSIT", result.getTransactions().get(0).getType());
        assertEquals(new BigDecimal("100.00"), result.getTransactions().get(0).getAmount());
        assertEquals(LocalDateTime.of(2024, 1, 2, 10, 0), result.getTransactions().get(0).getDate());
        assertEquals(101L, result.getTransactions().get(1).getTransactionId());
        assertEquals("WITHDRAWAL", result.getTransactions().get(1).getType());
    }
}