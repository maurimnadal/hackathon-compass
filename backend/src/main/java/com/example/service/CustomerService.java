package com.example.service;

import com.example.dto.CustomerDTO;
import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
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
        validateCustomerData(customerDTO);

        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail().trim().toLowerCase());
        customer.setBirthday(parseBirthday(customerDTO.getBirthday()));

        return customerRepository.save(customer);
    }

    private void validateCustomerData(CustomerDTO customerDTO) {
        // Validate email
        if (customerDTO.getEmail() == null || customerDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        String email = customerDTO.getEmail().trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Check if email is already registered
        if (customerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Validate birthday
        if (customerDTO.getBirthday() == null || customerDTO.getBirthday().length() != 8) {
            throw new IllegalArgumentException("Birthday must be 8 digits in YYYYMMDD format");
        }

        try {
            LocalDate birthday = parseBirthday(customerDTO.getBirthday());
            LocalDate now = LocalDate.now();

            if (birthday.isAfter(now)) {
                throw new IllegalArgumentException("Birthday cannot be in the future");
            }

            if (birthday.getYear() < 1900) {
                throw new IllegalArgumentException("Year must be 1900 or later");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid birthday format. Must be YYYYMMDD");
        }
    }

    private LocalDate parseBirthday(String birthdayStr) {
        if (birthdayStr == null || birthdayStr.length() != 8) {
            throw new IllegalArgumentException("Birthday must be 8 digits in YYYYMMDD format");
        }

        try {
            return LocalDate.parse(birthdayStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid birthday format. Must be YYYYMMDD");
        }
    }
}