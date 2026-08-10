package com.example.demo.service;

import com.example.demo.dto.response.TransactionResponse;
import com.example.demo.entity.Account;
import com.example.demo.entity.Transaction;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper mapper;

    public List<TransactionResponse> getTransactionsByAccountNumber(String accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber).
                orElseThrow(()->
                        new AccountNotFoundException(
                        "Account not found with account number: " + accountNumber)
                );

        List<Transaction> transactions = transactionRepository.findBySenderAccountOrReceiverAccount(account,account);
        return transactions.stream()
                .map(mapper::toResponse)
                .toList();
    }
}
