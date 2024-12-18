package com.example.lms.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

public class CourseService {
    @Service
    public static class JWTServiceImp {


        private String generateToken(UserDetails userDetails) {
            return Jwts.builder().setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        public String extractUserName(String token){
            return extractClaim(token, Claims::getSubject);
        }

        private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction){
            final Claims claims = extractAllClaims(token);
            return claimsTFunction.apply(claims);
        }
        private Key getSignKey(){
            byte[] key = Decoders.BASE64.decode("413F4428472B4B6250655368566D5970337336763979244226452948404D6351");
            return Keys.hmacShaKeyFor(key);
        }

        private Claims extractAllClaims(String token){
            return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        }
    }
}
