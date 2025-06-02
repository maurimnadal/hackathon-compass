package com.example.service;

import com.example.dto.CustomerDTO;
import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import com.example.util.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@]+@[^@]+\\.[^@]+$");

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(CustomerDTO customerDTO) throws IllegalArgumentException {
        validateCustomerData(customerDTO, null);

        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail().trim().toLowerCase());
        customer.setBirthday(DateValidator.parseDate(customerDTO.getBirthday()));

        return customerRepository.save(customer);
    }

    private void validateCustomerData(CustomerDTO customerDTO, Long idCostumer) {
        // Validate name
        if (customerDTO.getName() == null || customerDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        // Validate email
        if (customerDTO.getEmail() == null || customerDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        String email = customerDTO.getEmail().trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Check if email is already registered
        Optional<Customer> existing = customerRepository.findByEmail(email);
        if (existing.isPresent()) {
            Long existingId = existing.get().getId();
            if (!existingId.equals(idCostumer)) {
                throw new IllegalArgumentException("Email is already registered.");
            }
        }

        // Validate birthday
        DateValidator.validateDate(customerDTO.getBirthday());
    }
}