package com.example.demo.exception;

public class CustomerHasAccountException extends RuntimeException{
    public CustomerHasAccountException(String message){
        super(message);
    }
}
