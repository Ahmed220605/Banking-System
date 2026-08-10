package com.example.demo.exception;

public class SameAccountNumberException extends RuntimeException{
    public SameAccountNumberException(String message){
        super(message);
    }
}
