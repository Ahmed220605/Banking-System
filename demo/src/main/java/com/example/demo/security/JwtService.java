package com.example.demo.security;

import com.example.demo.enums.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username, Role role){
        Date date = new Date(System.currentTimeMillis()+30*60*1000);
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().claim("username",username)
                .claim("role",role)
                .signWith(key)
                .expiration(date).compact();
    }
}
