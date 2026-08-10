package com.example.demo.mapper;

import com.example.demo.dto.request.CreateAccountRequest;
import com.example.demo.dto.response.AccountResponse;
import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class AccountMapper{


    public Account toEntity(CreateAccountRequest request, Customer customer){
        Account account = new Account();

        account.setCustomer(customer);
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getBalance());

        return account;
    }

    public AccountResponse toResponse(Account account){
        AccountResponse response = new AccountResponse();

        response.setAccountNumber(account.getAccountNumber());
        response.setHolderName(account.getCustomer().getName());
        response.setAccountType(account.getAccountType());
        response.setAccountStatus(account.getAccountStatus());
        response.setBalance(account.getBalance());

        return response;
    }
}
