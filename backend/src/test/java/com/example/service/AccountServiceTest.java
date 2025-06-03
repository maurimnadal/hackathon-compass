package com.example.service;

import com.example.dto.AccountDTO;
import com.example.dto.TransactionDTO;
import com.example.model.Account;
import com.example.model.Customer;
import com.example.model.Transaction;
import com.example.repository.AccountRepository;
import com.example.repository.CustomerRepository;
import com.example.repository.TransactionRepository;
import com.example.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // createAccount tests

    @Test
    void createAccount_InvalidCustomerId_ThrowsException() {
        // Teste para ID nulo
        final AccountDTO dto1 = new AccountDTO(null, "S");
        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(dto1));

        // Teste para ID zero
        final AccountDTO dto2 = new AccountDTO(0L, "S");
        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(dto2));

        // Teste para ID muito grande
        final AccountDTO dto3 = new AccountDTO(100000L, "S");
        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(dto3));
    }

    @Test
    void createAccount_InvalidType_ThrowsException() {
        // Teste para tipo inválido
        final AccountDTO dto1 = new AccountDTO(1L, "X");
        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(dto1));
        
        // Teste para tipo nulo
        final AccountDTO dto2 = new AccountDTO(1L, null);
        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(dto2));
    }

    @Test
    void createAccount_CustomerNotFound_ThrowsException() {
        AccountDTO dto = new AccountDTO(1L, "S");
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(dto));
    }

    @Test
    void createAccount_ValidSavingsAccount_Success() {
        Customer customer = new Customer();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        AccountDTO dto = new AccountDTO(1L, "S");
        Account result = accountService.createAccount(dto);

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertEquals(Account.AccountType.SAVINGS, result.getAccountType());
    }

    @Test
    void createAccount_ValidCheckingAccount_Success() {
        Customer customer = new Customer();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        AccountDTO dto = new AccountDTO(1L, "C");
        Account result = accountService.createAccount(dto);

        assertNotNull(result);
        assertEquals(Account.AccountType.CHECKING, result.getAccountType());
    }

    // getAccountsByCustomerId tests

    @Test
    void getAccountsByCustomerId_InvalidId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> accountService.getAccountsByCustomerId(null));
        assertThrows(IllegalArgumentException.class, () -> accountService.getAccountsByCustomerId(0L));
        assertThrows(IllegalArgumentException.class, () -> accountService.getAccountsByCustomerId(100000L));
    }

    @Test
    void getAccountsByCustomerId_CustomerNotFound_ThrowsException() {
        when(customerRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> accountService.getAccountsByCustomerId(1L));
    }

    @Test
    void getAccountsByCustomerId_ValidCustomer_ReturnsAccounts() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(accountRepository.findByCustomerId(1L)).thenReturn(List.of(new Account()));

        List<Account> accounts = accountService.getAccountsByCustomerId(1L);
        assertEquals(1, accounts.size());
    }

    // processDeposit tests

    @Test
    void processDeposit_InvalidAmount_ThrowsException() {
        // Teste para valor nulo
        final TransactionDTO dto1 = new TransactionDTO(1L, null);
        assertThrows(IllegalArgumentException.class, () -> accountService.processDeposit(dto1));

        // Teste para valor muito pequeno
        final TransactionDTO dto2 = new TransactionDTO(1L, new BigDecimal("0.001"));
        assertThrows(IllegalArgumentException.class, () -> accountService.processDeposit(dto2));

        // Teste para valor muito grande
        final TransactionDTO dto3 = new TransactionDTO(1L, new BigDecimal("1000000"));
        assertThrows(IllegalArgumentException.class, () -> accountService.processDeposit(dto3));
    }
    
    @Test
    void validateTransactionAmount_InvalidAmount_ThrowsException() {
        // Verifica o método privado indiretamente através do método público
        final TransactionDTO dto1 = new TransactionDTO(1L, new BigDecimal("0"));
        assertThrows(IllegalArgumentException.class, () -> accountService.processDeposit(dto1));
        
        final TransactionDTO dto2 = new TransactionDTO(1L, new BigDecimal("999999.999"));
        assertThrows(IllegalArgumentException.class, () -> accountService.processDeposit(dto2));
    }

    @Test
    void processDeposit_AccountNotFound_ThrowsException() {
        TransactionDTO dto = new TransactionDTO(1L, new BigDecimal("100"));
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> accountService.processDeposit(dto));
    }

    @Test
    void processDeposit_ValidDeposit_Success() {
        Account account = new Account();
        account.setBalance(new BigDecimal("100"));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        TransactionDTO dto = new TransactionDTO(1L, new BigDecimal("50"));
        Transaction tx = accountService.processDeposit(dto);

        assertEquals(new BigDecimal("150"), account.getBalance());
        assertEquals(Transaction.TransactionType.DEPOSIT, tx.getType());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(any(Transaction.class));
    }

    // Process With drawal tests

    @Test
    void processWithdrawal_InvalidAmount_ThrowsException() {
        // Teste para valor nulo
        final TransactionDTO dto1 = new TransactionDTO(1L, null);
        assertThrows(IllegalArgumentException.class, () -> accountService.processWithdrawal(dto1));

        // Teste para valor muito pequeno
        final TransactionDTO dto2 = new TransactionDTO(1L, new BigDecimal("0.001"));
        assertThrows(IllegalArgumentException.class, () -> accountService.processWithdrawal(dto2));

        // Teste para valor muito grande
        final TransactionDTO dto3 = new TransactionDTO(1L, new BigDecimal("1000000"));
        assertThrows(IllegalArgumentException.class, () -> accountService.processWithdrawal(dto3));
    }

    @Test
    void processWithdrawal_AccountNotFound_ThrowsException() {
        TransactionDTO dto = new TransactionDTO(1L, new BigDecimal("100"));
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> accountService.processWithdrawal(dto));
    }

    @Test
    void processWithdrawal_InsufficientFunds_ThrowsException() {
        Account account = new Account();
        account.setBalance(new BigDecimal("50"));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        TransactionDTO dto = new TransactionDTO(1L, new BigDecimal("100"));
        assertThrows(IllegalArgumentException.class, () -> accountService.processWithdrawal(dto));
    }

    @Test
    void processWithdrawal_ValidWithdrawal_Success() {
        Account account = new Account();
        account.setBalance(new BigDecimal("200"));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        TransactionDTO dto = new TransactionDTO(1L, new BigDecimal("50"));
        Transaction tx = accountService.processWithdrawal(dto);

        assertEquals(new BigDecimal("150"), account.getBalance());
        assertEquals(Transaction.TransactionType.WITHDRAWAL, tx.getType());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(any(Transaction.class));
    }

    // findById tests

    @Test
    void findById_AccountFound() {
        Account account = new Account();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Optional<Account> result = accountService.findById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void findById_AccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Account> result = accountService.findById(1L);
        assertFalse(result.isPresent());
    }
}