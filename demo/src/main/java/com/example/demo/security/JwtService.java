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
        SecretKey key = getSigningKey();
        return Jwts.builder().claim("username",username)
                .claim("role",role)
                .signWith(key)
                .expiration(date).compact();
    }
    public String readUsername(String token){
        SecretKey key = getSigningKey();
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public Role readRole(String token){
        SecretKey key = getSigningKey();
        return Role.valueOf(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("role",String.class));
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
