package com.example.demo.service;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.entity.Customer;
import com.example.demo.exception.CustomerHasAccountException;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CustomerService {
    private final CustomerMapper mapper;
    private final CustomerRepository repository;

    public CustomerResponse createCustomer(CustomerRequest request){
        Customer customer = mapper.toEntity(request);
        Customer saveCustomer = repository.save(customer);
        return mapper.toResponse(saveCustomer);
    }

    public CustomerResponse getCustomerById(Long id){
        Customer customer = getCustomer(id);
        return mapper.toResponse(customer);
    }

    public List<CustomerResponse> getAllCustomers() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public CustomerResponse updateCustomer(Long id,CustomerRequest request){
            Customer customer = getCustomer(id);
            customer.setName(request.getName());
            customer.setEmail(request.getEmail());
            customer.setPhone(request.getPhone());
            Customer savedCustomer = repository.save(customer);
            return mapper.toResponse(savedCustomer);

    }

    public String deleteCustomer(Long id){
        Customer customer = getCustomer(id);
        if(!customer.getAccounts().isEmpty()){
            throw new CustomerHasAccountException("Customer has active accounts and cannot be deleted.");
        }
        repository.delete(customer);
        return "Customer deleted successfully";
    }

    private Customer getCustomer(Long id){
        return repository.findById(id).
                orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with this id " + id));
    }
}
