package com.example.controller;

import com.example.dto.CustomerDTO;
import com.example.model.Customer;
import com.example.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCustomer_Success() {
        CustomerDTO dto = new CustomerDTO("Alice", "alice@example.com", "19900101");
        Customer saved = new Customer();
        when(customerService.createCustomer(dto)).thenReturn(saved);

        ResponseEntity<?> response = customerController.createCustomer(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(saved, response.getBody());
    }

    @Test
    public void testCreateCustomer_Invalid() {
        CustomerDTO dto = new CustomerDTO("", "invalid", "19900101");
        when(customerService.createCustomer(dto)).thenThrow(new IllegalArgumentException("Invalid input"));

        ResponseEntity<?> response = customerController.createCustomer(dto);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((String) ((java.util.Map<?, ?>) response.getBody()).get("error")).contains("Invalid input"));
    }

    @Test
    public void testObterCliente_Found() {
        Customer customer = new Customer();
        customer.setName("Alice");
        customer.setEmail("alice@example.com");
        customer.setBirthday(LocalDate.of(1990, 1, 1));

        when(customerService.findById(1L)).thenReturn(Optional.of(customer));

        ResponseEntity<CustomerDTO> response = customerController.obterCliente(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Alice", response.getBody().getName());
        assertEquals("alice@example.com", response.getBody().getEmail());
        assertEquals("1990-01-01", response.getBody().getBirthday());
    }

    @Test
    public void testObterCliente_NotFound() {
        when(customerService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<CustomerDTO> response = customerController.obterCliente(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateCustomer_Success() {
        CustomerDTO dto = new CustomerDTO("Bob", "bob@example.com", "19851212");
        Customer updated = new Customer();

        when(customerService.updateCustomer(1L, dto)).thenReturn(updated);

        ResponseEntity<?> response = customerController.updateCustomer(1L, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updated, response.getBody());
    }

    @Test
    public void testUpdateCustomer_Invalid() {
        CustomerDTO dto = new CustomerDTO("Bob", "bob@example.com", "19851212");

        when(customerService.updateCustomer(1L, dto)).thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<?> response = customerController.updateCustomer(1L, dto);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((String) ((java.util.Map<?, ?>) response.getBody()).get("error")).contains("Invalid data"));
    }
}