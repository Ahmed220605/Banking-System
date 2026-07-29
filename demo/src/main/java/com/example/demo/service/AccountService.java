package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository){
        this.repository = repository;
    }

    public boolean createAccount(Account account){
        Account existingAccount = repository.findByAccountNo(account.getAccountNo());
        if(existingAccount != null){
            return false;
        }
        repository.save(account);
        return true;
    }

    public boolean deposit(int accountNo,double amount){
        Account account = repository.findByAccountNo(accountNo);
        if(account == null){
            return false;
        }

        if(amount <= 0){
            return false;
        }
            account.setBalance(account.getBalance() + amount);
            return true;
    }

    public boolean withdraw(int accountNo,double amount){
        Account account = repository.findByAccountNo(accountNo);
        if(account == null){
            return false;
        }

        if(amount <= 0){
            return false;
        }
        if(account.getBalance() < amount){
            return false;
        }
        account.setBalance(account.getBalance() - amount);
        return true;
    }

    public boolean transfer(int sendingAccountNo, int receivingAccountNo, double amount){
        Account sendingAccount = repository.findByAccountNo(sendingAccountNo);
        Account receivingAccount = repository.findByAccountNo(receivingAccountNo);
        if(sendingAccountNo==receivingAccountNo){
            return false;
        }
        if(sendingAccount == null || receivingAccount == null){
            return false;
        }
        if(amount <= 0){
            return false;
        }
        if(sendingAccount.getBalance() < amount){
            return false;
        }
        if(!withdraw(sendingAccountNo,amount)){
            return false;
        }
        if(!deposit(receivingAccountNo,amount)){
            return false;
        }
        return true;
    }
}
