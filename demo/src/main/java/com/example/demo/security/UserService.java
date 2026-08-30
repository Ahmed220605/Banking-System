package com.example.demo.security;

import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Customer;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final UserMapper mapper;


    public UserResponse createUser(UserRequest request){
        User user = mapper.toEntity(request);
        user = userRepository.save(user);

        if(user.getRole() == Role.CUSTOMER){
            Customer customer = new Customer();
            customer.setName(request.getName());
            customer.setEmail(request.getEmail());
            customer.setPhone(request.getPhone());
            customer.setUser(user);
            customerRepository.save(customer);
            return mapper.toResponseCustomer(user,customer);
        }

        return mapper.toResponseAdmin(user);
    }
}
