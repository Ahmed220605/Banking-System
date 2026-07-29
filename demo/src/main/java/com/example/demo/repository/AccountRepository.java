package com.example.demo.repository;

import com.example.demo.model.Account;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AccountRepository {
    private Map<Integer, Account> accounts = new HashMap<>();

    public void save(Account account){
        accounts.put(account.getAccountNo(), account);
    }

    public Account findByAccountNo(int accountNo) {
        return accounts.get(accountNo);
    }

    public Collection<Account> findAll() {
        return accounts.values();
    }

    public void delete(int accountNo){
        accounts.remove(accountNo);
    }
}
