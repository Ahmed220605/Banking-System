package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.enums.AccountStatus;
import com.example.demo.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByAccountType(AccountType accountType);
    List<Account> findByAccountStatus(AccountStatus accountStatus);
    List<Account> findByCustomerId(Long id);

}
