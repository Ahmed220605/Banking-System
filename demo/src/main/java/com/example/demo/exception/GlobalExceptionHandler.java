package com.example.demo.exception;

import com.example.demo.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleAccountNotFound(AccountNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiResponse<?>> handleInsufficientBalance(InsufficientBalanceException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(InvalidAccountStatusException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidAccountStatus(InvalidAccountStatusException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(SameAccountNumberException.class)
    public ResponseEntity<ApiResponse<?>> handleSameAccountNumber(SameAccountNumberException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomerNotFound(CustomerNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(CustomerHasAccountException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomerHasAccount(CustomerHasAccountException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFound(UsernameNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(
                        false,
                        "Validation failed",
                        errors
                ));
    }

}
