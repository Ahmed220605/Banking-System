package com.example.demo.exception;

public class RoleNotMatchedException extends RuntimeException {
    public RoleNotMatchedException(String message) {
        super(message);
    }
}
