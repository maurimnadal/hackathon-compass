package com.example.controller;

import com.example.dto.AccountDTO;
import com.example.dto.AccountResponseDTO;
import com.example.dto.CustomerAccountsDTO;
import com.example.dto.TransactionDTO;
import com.example.dto.TransactionResponseDTO;
import com.example.model.Account;
import com.example.model.Customer;
import com.example.model.Transaction;
import com.example.repository.CustomerRepository;
import com.example.service.AccountService;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final CustomerRepository customerRepository;

    @Autowired
    public AccountController(AccountService accountService, TransactionService transactionService, CustomerRepository customerRepository) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.customerRepository = customerRepository;
    }

    // Endpoint HTTP POST para criar uma nova conta a partir dos dados enviados no corpo da requisição
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO accountDTO) {
        try {
            Account account = accountService.createAccount(accountDTO);
            return new ResponseEntity<>(account, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    // Endpoint HTTP GET para buscar contas associadas a um cliente específico pelo ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getAccountsByCustomerId(@PathVariable Long customerId) {
        try {
            // Validar se o cliente existe
            Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
            
            // Buscar as contas do cliente
            List<Account> accounts = accountService.getAccountsByCustomerId(customerId);
            
            // Converter contas para AccountResponseDTO (apenas id, tipo e saldo)
            // Se não houver contas, a lista estará vazia, mas será retornada mesmo assim
            List<AccountResponseDTO> accountDTOs = accounts.stream()
                .map(account -> new AccountResponseDTO(
                    account.getId(),
                    account.getAccountType().toString(),
                    account.getBalance()
                ))
                .collect(Collectors.toList());
            
            // Criar DTO com informações do cliente e lista de contas (que pode estar vazia)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            CustomerAccountsDTO response = new CustomerAccountsDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getBirthday().format(formatter),
                accountDTOs
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long accountId, @RequestBody TransactionDTO transactionDTO) {
        try {
            transactionDTO.setAccountId(accountId);
            Transaction transaction = accountService.processDeposit(transactionDTO);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> obterConta(@PathVariable("accountId") Long id) {
        return accountService.findById(id)
                .map(conta -> {
                    AccountResponseDTO dto = new AccountResponseDTO(
                        conta.getId(),
                        conta.getAccountType().toString(),
                        conta.getBalance()
                    );
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<?> sacar(@PathVariable("accountId") Long id, @RequestBody TransactionDTO transactionDTO) {
        try {
            if (transactionDTO.getAccountId() == null) {
                transactionDTO = new TransactionDTO(id, transactionDTO.getAmount());
            } else if (!transactionDTO.getAccountId().equals(id)) {
                return ResponseEntity.badRequest().body("ID da conta não correspondente");
            }
            
            Transaction transaction = accountService.processWithdrawal(transactionDTO);
            
            TransactionResponseDTO responseDTO = new TransactionResponseDTO(
                transaction.getAccount().getId(),
                transaction.getAmount(),
                transaction.getAccount().getBalance(),
                transaction.getTimestamp()
            );
            
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}