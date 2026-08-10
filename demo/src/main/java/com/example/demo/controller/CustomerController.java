package com.example.demo.controller;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.response.AccountResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.service.AccountService;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService service;
    private final AccountService accountService;

    @PostMapping()
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                true,
                "Customer created successfully",
                service.createCustomer(request)
        ));
    }
    @GetMapping()
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers(){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Customers retrieved successfully",
                service.getAllCustomers()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable Long id){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Customer retrieved successfully",
                service.getCustomerById(id)
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(@PathVariable Long id,@Valid @RequestBody CustomerRequest request){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Customer updated successfully",
                service.updateCustomer(id,request)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable Long id){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                service.deleteCustomer(id),
                null
        ));
    }

    @GetMapping("/{id}/accounts")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAccountsByCustomerId(@PathVariable Long id){
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Customer accounts retrieved successfully",
                accountService.getAccountsByCustomerId(id)
        ));
    }
}
