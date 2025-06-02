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

    public Customer updateCustomer(Long id, CustomerDTO customerDTO) throws IllegalArgumentException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id " + id));

        validateCustomerData(customerDTO, id);

        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail().trim().toLowerCase());
        customer.setBirthday(DateValidator.parseDate(customerDTO.getBirthday()));

        return customerRepository.save(customer);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    private void validateCustomerData(CustomerDTO customerDTO, Long idCustomer) {
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
            if (idCustomer == null || !existingId.equals(idCustomer)) {
                throw new IllegalArgumentException("Email is already registered.");
            }
        }

        // Validate birthday
        DateValidator.validateDate(customerDTO.getBirthday());
    }
}