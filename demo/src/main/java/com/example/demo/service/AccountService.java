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
import com.example.demo.enums.Role;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String authority = authentication.getAuthorities().stream().findFirst().orElseThrow().getAuthority();
        Role role = Role.valueOf(
                authority.replace("ROLE_", "")
        );
        Customer customer;
        if(Role.CUSTOMER.equals(role)){
            customer = customerRepository.findByUser_Username(username).
                    orElseThrow(()-> new CustomerNotFoundException(
                            "Customer not found for username: " + username));
        }else {
            customer = customerRepository.findById(request.getCustomerId()).
                    orElseThrow(()-> new CustomerNotFoundException(
                            "Customer not found for this id: " + request.getCustomerId()));
        }
        Account account = mapper.toEntity(request,customer);

        account.setAccountStatus(AccountStatus.ACTIVE);
        Account savedAccount = repository.save(account);

        savedAccount.setAccountNumber(generateAccountNumber(account.getId()));
        savedAccount = repository.save(savedAccount);
        AccountResponse response = mapper.toResponse(savedAccount);
        System.out.println(response.getHolderName());
        return response;
    }

    public AccountResponse getAccountByAccountNumber(String accountNumber){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String authority = authentication.getAuthorities().stream().findFirst().orElseThrow().getAuthority();
        Role role = Role.valueOf(
                authority.replace("ROLE_", "")
        );
        Account account = getActiveAccount(accountNumber);
        if(Role.ADMIN.equals(role)){
            return mapper.toResponse(account);
        } else if (Role.CUSTOMER.equals(role)) {
            if(account.getCustomer().getUser().getUsername().equals(username)){
                return mapper.toResponse(account);
            }else{
                throw new UnauthorizedAccountAccessException("Unauthorized account access");
            }
        }

        throw new RoleNotMatchedException("Role not match");
    }

    public List<AccountResponse> getAllAccounts() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public AccountResponse depositAmount(String accountNumber, DepositRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        String authority = authentication.getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow()
                .getAuthority();

        Role role = Role.valueOf(
                authority.replace("ROLE_", "")
        );

        Account account = getActiveAccount(accountNumber);

        // Authorization check
        if (Role.ADMIN.equals(role)) {
            // ADMIN is allowed to deposit into any account

        } else if (Role.CUSTOMER.equals(role)) {

            String accountOwnerUsername =
                    account.getCustomer().getUser().getUsername();

            if (!accountOwnerUsername.equals(username)) {
                throw new UnauthorizedAccountAccessException(
                        "Unauthorized account access"
                );
            }

        } else {
            throw new RoleNotMatchedException("Role not match");
        }

        // Deposit operation
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidAccountStatusException(
                    "Deposits are only allowed for ACTIVE accounts."
            );
        }

        account.setBalance(
                account.getBalance().add(request.getAmount())
        );

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
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        String authority = authentication.getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow()
                .getAuthority();

        Role role = Role.valueOf(
                authority.replace("ROLE_", "")
        );

        Account account = getActiveAccount(accountNumber);

        if (Role.ADMIN.equals(role)) {

        } else if (Role.CUSTOMER.equals(role)) {

            String accountOwnerUsername =
                    account.getCustomer().getUser().getUsername();

            if (!accountOwnerUsername.equals(username)) {
                throw new UnauthorizedAccountAccessException(
                        "Unauthorized account access"
                );
            }

        } else {
            throw new RoleNotMatchedException("Role not match");
        }

        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidAccountStatusException(
                    "Withdrawals are only allowed for ACTIVE accounts."
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
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        String authority = authentication.getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow()
                .getAuthority();

        Role role = Role.valueOf(
                authority.replace("ROLE_", "")
        );

        Account senderAccount = getActiveAccount(senderAccountNumber);
        Account receiverAccount = getActiveAccount(request.getReceiverAccountNumber());
        if (Role.ADMIN.equals(role)) {

        }else if(Role.CUSTOMER.equals(role)){
            if(!senderAccount.getCustomer().getUser().getUsername().equals(username)){
                throw new UnauthorizedAccountAccessException("Unauthorized account access");
            }
        }else{
            throw new RoleNotMatchedException("Role not match");
        }



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
