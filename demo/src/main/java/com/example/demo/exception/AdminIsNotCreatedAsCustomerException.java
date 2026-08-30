package com.example.demo.exception;

public class AdminIsNotCreatedAsCustomerException extends RuntimeException{
    public AdminIsNotCreatedAsCustomerException(String message){
        super(message);
    }
}
