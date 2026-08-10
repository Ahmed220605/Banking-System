package com.example.demo.controller;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.TransactionResponse;
import com.example.demo.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/accounts")
public class TransactionController {
    private final TransactionService service;
    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionByAccountNumber(@PathVariable String accountNumber){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Transaction history retrieved successfully",
                service.getTransactionsByAccountNumber(accountNumber)));
    }
}
