package com.example.demo.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder encoderPassword(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider provider){
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.csrf(csrf->csrf.disable());
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/auth/register").permitAll()
                .requestMatchers(HttpMethod.GET,"/accounts").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST,"/accounts").hasAnyAuthority("ROLE_ADMIN","ROLE_CUSTOMER")
                .requestMatchers(HttpMethod.GET, "/accounts/{accountNumber}").hasAnyAuthority("ROLE_ADMIN","ROLE_CUSTOMER")
                .requestMatchers(HttpMethod.PATCH, "/accounts/{accountNumber}/deposit").hasAnyAuthority("ROLE_ADMIN","ROLE_CUSTOMER")
                .requestMatchers(HttpMethod.PATCH, "/accounts/{accountNumber}/withdraw").hasAnyAuthority("ROLE_ADMIN","ROLE_CUSTOMER")
                .requestMatchers(HttpMethod.PATCH, "/accounts/{accountNumber}/transfer").hasAnyAuthority("ROLE_ADMIN","ROLE_CUSTOMER")
                .anyRequest().authenticated()).build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
