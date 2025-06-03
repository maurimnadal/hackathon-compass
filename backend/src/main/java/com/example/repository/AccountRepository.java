package com.example.repository;

import com.example.model.Account;
import com.example.model.Customer; // Importa a classe Customer usada nas consultas por client
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importa a interface List para retornar listas de contas

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomer(Customer customer); //  Busca contas com base em um objeto Customer
    List<Account> findByCustomerId(Long customerId); // Busca contas com base no ID do Customer
}