package com.example.controller;

import com.example.dto.AccountDTO;
import com.example.dto.TransactionDTO;
import com.example.dto.AccountResponseDTO;
import com.example.dto.TransactionResponseDTO;
import com.example.model.Account;
import com.example.model.Transaction;
import com.example.service.AccountService;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List; 
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @Autowired
    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
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
            List<Account> accounts = accountService.getAccountsByCustomerId(customerId);
            return new ResponseEntity<>(accounts, HttpStatus.OK);
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