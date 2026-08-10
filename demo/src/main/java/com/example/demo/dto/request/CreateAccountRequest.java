package com.example.demo.dto.request;

import com.example.demo.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CreateAccountRequest {
    @NotNull
    private Long customerId;
    @PositiveOrZero
    private BigDecimal balance;
    @NotNull
    private AccountType accountType;
}
