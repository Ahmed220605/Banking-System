package com.example.demo.dto.response;

import com.example.demo.enums.Role;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserResponse {
    private String username;
    private Role role;
    private String name;
    private String email;
    private String phone;
}
