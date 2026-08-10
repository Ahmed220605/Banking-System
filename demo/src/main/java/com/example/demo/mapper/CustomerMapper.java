package com.example.demo.mapper;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.response.AccountSummaryResponse;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request){
        Customer customer = new Customer();

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        return customer;
    }

    public CustomerResponse toResponse(Customer customer){
        CustomerResponse response = new CustomerResponse();

        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAccounts(customer.getAccounts().stream().map(this::toAccountSummary).toList());

        return response;
    }

    private AccountSummaryResponse toAccountSummary(Account account){
        AccountSummaryResponse response = new AccountSummaryResponse();

        response.setAccountNumber(account.getAccountNumber());
        response.setBalance(account.getBalance());
        response.setAccountType(account.getAccountType());
        response.setAccountStatus(account.getAccountStatus());

        return response;
    }

}
