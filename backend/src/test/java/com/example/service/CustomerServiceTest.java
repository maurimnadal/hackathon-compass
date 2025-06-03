package com.example.service;

import com.example.dto.CustomerDTO;
import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import com.example.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

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

    // createCustomer tests

    @Test
    void testCreateCustomer_NameNull_ThrowsException() {
        final CustomerDTO dto = new CustomerDTO(null, "email@example.com", "19900101");
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void testCreateCustomer_NameEmpty_ThrowsException() {
        final CustomerDTO dto = new CustomerDTO("", "email@example.com", "19900101");
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void testCreateCustomer_EmailNull_ThrowsException() {
        final CustomerDTO dto = new CustomerDTO("Name", null, "19900101");
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void testCreateCustomer_EmailEmpty_ThrowsException() {
        final CustomerDTO dto = new CustomerDTO("Name", "", "19900101");
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void testCreateCustomer_EmailInvalid_ThrowsException() {
        final CustomerDTO dto = new CustomerDTO("Name", "invalid", "19900101");
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void testCreateCustomer_EmailAlreadyExists_ThrowsException() {
        final CustomerDTO dto = new CustomerDTO("Name", "john@example.com", "19900101");
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void testCreateCustomer_InvalidBirthDate_ThrowsException() {
        final CustomerDTO dto = new CustomerDTO("Name", "john@example.com", null);
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void testCreateCustomer_InvalidBirthDateFormat_ThrowsException() {
        final CustomerDTO dto = new CustomerDTO("Name", "john@example.com", "1990-01-01");
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void testCreateCustomer_Valid_ReturnsCustomer() {
        final CustomerDTO dto = new CustomerDTO("John Doe", "john@example.com", "19900101");
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenReturn(customer);
        
        Customer result = customerService.createCustomer(dto);
        
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthday());
    }

    // updateCustomer tests

    @Test
    void testUpdateCustomer_NotFound_ThrowsException() {
        final CustomerDTO dto = new CustomerDTO("Updated", "new@example.com", "19900101");
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomer(1L, dto));
    }

    @Test
    void testUpdateCustomer_NameNull_ThrowsException() {
        final CustomerDTO dto = new CustomerDTO(null, "email@example.com", "19900101");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomer(1L, dto));
    }

    @Test
    void testUpdateCustomer_EmailInvalid_ThrowsException() {
        final CustomerDTO dto = new CustomerDTO("Name", "bad-email", "19900101");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomer(1L, dto));
    }

    @Test
    void testUpdateCustomer_EmailAlreadyExists_ThrowsException() {
        final CustomerDTO dto = new CustomerDTO("Name", "new@example.com", "19900101");
        
        Customer existingCustomer = new Customer();
        existingCustomer.setId(2L);
        existingCustomer.setEmail("new@example.com");
        
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.findByEmail("new@example.com")).thenReturn(Optional.of(existingCustomer));

        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomer(1L, dto));
    }

    @Test
    void testUpdateCustomer_SameEmailDifferentCase_Success() {
        final CustomerDTO dto = new CustomerDTO("Updated Name", "JOHN@example.com", "19900101");
        
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = customerService.updateCustomer(1L, dto);
        
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void testUpdateCustomer_InvalidBirthDate_ThrowsException() {
        final CustomerDTO dto = new CustomerDTO("Name", "email@example.com", null);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomer(1L, dto));
    }

    @Test
    void testUpdateCustomer_Valid_ReturnsUpdatedCustomer() {
        final CustomerDTO dto = new CustomerDTO("Updated", "new@example.com", "19900101");
        
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = customerService.updateCustomer(1L, dto);

        assertNotNull(result);
        verify(customerRepository).save(customer);
    }

    // findById tests

    @Test
    void testFindById_Exists_ReturnsCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Optional<Customer> result = customerService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testFindById_NotExists_ReturnsEmpty() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Customer> result = customerService.findById(1L);
        assertTrue(result.isEmpty());
    }
}