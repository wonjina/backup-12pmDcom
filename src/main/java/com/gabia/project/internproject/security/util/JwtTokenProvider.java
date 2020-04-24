package com.gabia.project.internproject.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${spring.ouath.hiworks.jwtSecret}")
    private String jwtSecret;

    public Claims getJwtValue(String token) throws JwtException, IllegalArgumentException {
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        } catch (IllegalArgumentException exception) {
            throw exception;
        } catch (JwtException exception) {
            throw exception;
        }
    }

    public String generateHiworksJwtToken(Map<String, Object> headerMap, Map<String, Object> map) {
        return Jwts.builder()
                .setHeader(headerMap)
                .setIssuedAt(Date.from(Instant.now()))
                .setClaims(map)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }
}
