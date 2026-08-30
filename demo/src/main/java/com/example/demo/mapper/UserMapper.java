package com.example.demo.mapper;

import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Customer;
import com.example.demo.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User toEntity(UserRequest request){
        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        return user;
    }

    public UserResponse toResponseCustomer(User user, Customer customer){
        UserResponse response =  new UserResponse();

        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());

        return response;
    }

    public UserResponse toResponseAdmin(User user){
        UserResponse response = new UserResponse();

        response.setUsername(user.getUsername());
        response.setRole(user.getRole());

        return response;
    }
}
