package com.soumya.urlshortener.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtService {

    private final UserDetailsService userDetailsService;

    public JwtService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    public SecretKey secretKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String userName){
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        Map<String,Object> claims = new HashMap<>();
        String role = userDetails.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("ROLE_USER");
        claims.put("role",role);
        String token = createToken(claims,userName);
        if (token != null) log.info("Token generate successful for Username: {}",userName);
        else log.info("Token generate Unsuccessful for Username: {}",userName);
        return token;
    }

    public String createToken(Map<String,Object>claims,String userName){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(secretKey())
                .compact();
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }

    public boolean isExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token,UserDetails userDetails){
        boolean usernameMatch = extractUsername(token).equals(userDetails.getUsername());
        boolean expiration = isExpired(token);
        return usernameMatch && !expiration;
    }
}
