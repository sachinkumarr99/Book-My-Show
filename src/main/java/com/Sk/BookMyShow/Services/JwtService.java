package com.Sk.BookMyShow.Services;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;



@Service 
public class JwtService {

    private final SecretKey secretKey;
    private final Long expirationTimeMs;

    public JwtService(
        @Value ("${myApp.secretKey.value}")
        String secretKey,
        @Value ("${myApp.secretKey.expiration}")
        Long expirationTimeMs
    ){
        this.secretKey= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.expirationTimeMs=expirationTimeMs;
    }

    public String generateAccessToken(String email,Map<String,?> claims){

        Long now = System.currentTimeMillis();

        return Jwts.builder()
                    .subject(email)
                    .claims(claims)
                    .issuedAt(new Date(now))
                    .expiration(new Date(now + expirationTimeMs))
                    .signWith(secretKey)
                    .compact();

    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    public <T> T extractClaim(String token,Function<Claims,T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    
}
