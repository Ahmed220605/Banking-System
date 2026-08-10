package com.example.demo.service;

import com.example.demo.dto.request.CreateAccountRequest;
import com.example.demo.dto.request.DepositRequest;
import com.example.demo.dto.request.TransferRequest;
import com.example.demo.dto.request.WithdrawRequest;
import com.example.demo.dto.response.AccountResponse;
import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Transaction;
import com.example.demo.enums.AccountStatus;
import com.example.demo.enums.AccountType;
import com.example.demo.enums.TransactionType;
import com.example.demo.exception.*;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.repository.AccountRepository;

import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;


import java.util.List;

@Getter
@Setter
@AllArgsConstructor

@Service
public class AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;


    public AccountResponse createAccount(CreateAccountRequest request){
        Customer customer = customerRepository.findById(request.getCustomerId()).
                orElseThrow(()-> new CustomerNotFoundException(
                        "Customer not found with this id "+request.getCustomerId()));
        Account account = mapper.toEntity(request,customer);

        account.setAccountStatus(AccountStatus.ACTIVE);
        Account savedAccount = repository.save(account);

        savedAccount.setAccountNumber(generateAccountNumber(account.getId()));
        savedAccount = repository.save(savedAccount);

        return mapper.toResponse(savedAccount);
    }

    public AccountResponse getAccountByAccountNumber(String accountNumber){
        Account account = getActiveAccount(accountNumber);
        return mapper.toResponse(account);
    }

    public List<AccountResponse> getAllAccounts() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public AccountResponse depositAmount(String accountNumber, DepositRequest request){
        Account account = repository.findByAccountNumber(accountNumber).
                orElseThrow(()-> new AccountNotFoundException( "Account not found with account number: " + accountNumber));
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidAccountStatusException(
                    "Deposits are only allowed for ACTIVE accounts."
            );
        }
        account.setBalance(account.getBalance().add(request.getAmount()));

        Account savedAccount = repository.save(account);

        Transaction transaction = new Transaction();

        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setReceiverAccount(savedAccount);

        transactionRepository.save(transaction);

        return mapper.toResponse(savedAccount);
    }
    @Transactional
    public AccountResponse withdrawAmount(String accountNumber, WithdrawRequest request){
        Account account = getActiveAccount(accountNumber);
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidAccountStatusException(
                    "Withdraw are only allowed for ACTIVE accounts."
            );
        }
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }
        account.setBalance(account.getBalance().subtract(request.getAmount()));

        Account savedAccount = repository.save(account);

        Transaction transaction = new Transaction();

        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setSenderAccount(savedAccount);

        transactionRepository.save(transaction);

        return mapper.toResponse(savedAccount);
    }

    @Transactional
    public AccountResponse transferAmount(String senderAccountNumber, TransferRequest request){
        Account senderAccount = getActiveAccount(senderAccountNumber);
        Account receiverAccount = getActiveAccount(request.getReceiverAccountNumber());
        if(senderAccount.getAccountNumber().equals(receiverAccount.getAccountNumber())) {
            throw new SameAccountNumberException(
                    "Both are same account numbers."
            );
        }
        if (senderAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance."
            );
        }
        senderAccount.setBalance(senderAccount.getBalance().subtract(request.getAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(request.getAmount()));

        repository.save(receiverAccount);
        Account senderSavedAccount = repository.save(senderAccount);

        Transaction transaction = new Transaction();

        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setSenderAccount(senderSavedAccount);
        transaction.setReceiverAccount(receiverAccount);

        transactionRepository.save(transaction);

        return mapper.toResponse(senderSavedAccount);
    }
    public AccountResponse blockAccount(String accountNumber){
        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found with account number: " + accountNumber
                ));
        if(account.getAccountStatus() == AccountStatus.CLOSED){
            throw new InvalidAccountStatusException(
                    "Closed accounts cannot be blocked."
            );
        }
        if (account.getAccountStatus() == AccountStatus.BLOCKED) {
            throw new InvalidAccountStatusException(
                    "Account is already blocked."
            );
        }
        account.setAccountStatus(AccountStatus.BLOCKED);
        Account savedAccount = repository.save(account);

        return mapper.toResponse(savedAccount);
    }

    public AccountResponse unblockAccount(String accountNumber){
        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found with account number: " + accountNumber
                ));
        if(account.getAccountStatus() == AccountStatus.CLOSED){
            throw new InvalidAccountStatusException(
                    "Closed accounts cannot be unblocked."
            );
        }
        if (account.getAccountStatus() == AccountStatus.ACTIVE) {
            throw new InvalidAccountStatusException(
                    "Account is already active."
            );
        }
        account.setAccountStatus(AccountStatus.ACTIVE);
        Account savedAccount = repository.save(account);

        return mapper.toResponse(savedAccount);
    }

    public AccountResponse closeAccount(String accountNumber){
        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found with account number: " + accountNumber
                ));
        if (account.getAccountStatus() == AccountStatus.CLOSED) {
            throw new InvalidAccountStatusException(
                    "Account is already closed."
            );
        }
        account.setAccountStatus(AccountStatus.CLOSED);
        Account savedAccount = repository.save(account);

        return mapper.toResponse(savedAccount);
    }

    public List<AccountResponse> getAccountsByCustomerId(Long customerId){
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->
                new CustomerNotFoundException(
                "Customer not found with this id "+customerId));

        return repository.findByCustomerId(customerId).stream().map(mapper::toResponse).toList();

    }

    public List<AccountResponse> getAccountsByAccountType(AccountType accountType){
        if(accountType == null ){
            return repository.findAll().stream().map(mapper::toResponse).toList();
        }
        return repository.findByAccountType(accountType).stream().map(mapper::toResponse).toList();

    }


    private String generateAccountNumber(Long id){
        return String.format("ACC%06d", id);
    }

    private Account getActiveAccount(String accountNumber){
        return repository.findByAccountNumber(accountNumber).
                orElseThrow(()-> new AccountNotFoundException(
                        "Account not found with account number: " + accountNumber)
                );
    }
}
