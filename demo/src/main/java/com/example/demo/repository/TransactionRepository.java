package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findBySenderAccount(Account senderAccount);
    List<Transaction> findByReceiverAccount(Account receiverAccount);
    List<Transaction> findBySenderAccountOrReceiverAccount(Account senderAccount, Account receiverAccount);
}
