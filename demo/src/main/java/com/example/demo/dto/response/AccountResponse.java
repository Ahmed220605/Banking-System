package com.example.demo.dto.response;

import com.example.demo.enums.AccountStatus;
import com.example.demo.enums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AccountResponse {
    private String accountNumber;
    private String holderName;
    private BigDecimal balance;
    private AccountType accountType;
    private AccountStatus accountStatus;
}
