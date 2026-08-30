package com.example.demo.security;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.enums.Role;
import com.example.demo.exception.CustomerNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public LoginResponse login(LoginRequest request){
        LoginResponse response = new LoginResponse();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        System.out.println("AUTHENTICATION SUCCESS: " + authentication.getName());
        var authority = Role.valueOf(
                authentication.getAuthorities()
                        .stream()
                        .findFirst()
                        .orElseThrow()
                        .getAuthority()
                        .replace("ROLE_", ""));

        response.setToken(jwtService.generateToken(request.getUsername(),authority));

        return response;
    }


}
