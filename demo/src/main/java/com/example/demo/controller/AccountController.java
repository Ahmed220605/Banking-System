package com.example.demo.controller;

import com.example.demo.dto.request.CreateAccountRequest;
import com.example.demo.dto.request.DepositRequest;
import com.example.demo.dto.request.TransferRequest;
import com.example.demo.dto.request.WithdrawRequest;
import com.example.demo.dto.response.AccountResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.enums.AccountType;
import com.example.demo.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController()
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService service;

    @PostMapping()
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@Valid @RequestBody CreateAccountRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                true,
                "Account created successfully.",
                service.createAccount(request)));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccountByAccountNumber(@PathVariable String accountNumber){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Account retrieved successfully.",
                service.getAccountByAccountNumber(accountNumber)));
    }
    

    @GetMapping()
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAccountsByAccountType(
            @RequestParam(required = false) AccountType accountType) {

        System.out.println("Account Type: " + accountType);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Accounts retrieved successfully.",
                service.getAccountsByAccountType(accountType)
        ));
    }

    @PatchMapping("/{accountNumber}/block")
    public ResponseEntity<ApiResponse<AccountResponse>> blockAccount(@PathVariable String accountNumber){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Account block successfully",
                service.blockAccount(accountNumber)
        ));
    }
    @PatchMapping("/{accountNumber}/unblock")
    public ResponseEntity<ApiResponse<AccountResponse>> unblockAccount(@PathVariable String accountNumber){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Account unblocked successfully",
                service.unblockAccount(accountNumber)
        ));
    }
    @PatchMapping("/{accountNumber}/close")
    public ResponseEntity<ApiResponse<AccountResponse>> closedAccount(@PathVariable String accountNumber){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Account closed successfully",
                service.closeAccount(accountNumber)
        ));
    }

    @PatchMapping("/{accountNumber}/deposit")
    public ResponseEntity<ApiResponse<AccountResponse>> deposit(@PathVariable String accountNumber, @Valid @RequestBody DepositRequest request){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Deposit completed successfully.",
                service.depositAmount(accountNumber,request)));
    }

    @PatchMapping("/{accountNumber}/withdraw")
    public ResponseEntity<ApiResponse<AccountResponse>> withdraw(@PathVariable String accountNumber, @Valid @RequestBody WithdrawRequest request){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Withdraw completed successfully.",
                service.withdrawAmount(accountNumber,request)));
    }
    @PatchMapping("/{accountNumber}/transfer")
    public ResponseEntity<ApiResponse<AccountResponse>> transfer(@PathVariable String accountNumber, @Valid @RequestBody TransferRequest request){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Transfer completed successfully.",
                service.transferAmount(accountNumber,request)));
    }
}
