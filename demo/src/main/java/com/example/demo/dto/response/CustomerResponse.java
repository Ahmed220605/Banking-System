package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private List<AccountSummaryResponse> accounts;
}
