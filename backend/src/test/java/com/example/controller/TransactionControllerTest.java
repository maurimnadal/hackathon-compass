package com.example.controller;

import com.example.dto.TransactionReportDTO;
import com.example.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTransactionReport_Success() {
        Long customerId = 1L;
        String startDate = "2023-01-01";
        String endDate = "2023-12-31";
        TransactionReportDTO mockReport = new TransactionReportDTO();

        when(transactionService.generateTransactionReport(customerId, startDate, endDate)).thenReturn(mockReport);

        ResponseEntity<?> response = transactionController.getTransactionReport(customerId, startDate, endDate);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockReport, response.getBody());
    }
} 