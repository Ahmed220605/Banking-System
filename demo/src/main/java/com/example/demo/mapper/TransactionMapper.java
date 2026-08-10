package com.example.demo.mapper;

import com.example.demo.dto.response.TransactionResponse;
import com.example.demo.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class TransactionMapper {
    public TransactionResponse toResponse(Transaction transaction){
        TransactionResponse response = new TransactionResponse();

        response.setSenderAccountNumber(
                transaction.getSenderAccount() != null
                        ? transaction.getSenderAccount().getAccountNumber()
                        : null
        );
        response.setReceiverAccountNumber(
                transaction.getReceiverAccount() != null
                        ? transaction.getReceiverAccount().getAccountNumber()
                        : null
        );
        response.setAmount(transaction.getAmount());
        response.setTransactionType(transaction.getTransactionType());
        response.setTransactionDate(transaction.getTransactionDate());
        return response;
    }
}
