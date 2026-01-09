package org.example.securecorporatetalentportal.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class JwtService {

    private final Key key;
    private final int expMinutes;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expMinutes}") int expMinutes) {
        // Keys.hmacShaKeyFor(...) creates a signing key for HS256
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expMinutes = expMinutes;
    }

    public String generateToken(String username, List<String> roles) {
        Instant now = Instant.now();
        Instant exp = now.plus(expMinutes, ChronoUnit.MINUTES);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        // Jwts.builder() builds a JWT token (header.payload.signature)
        return Jwts.builder()
                .setSubject(username)                 // "sub" claim = username
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .addClaims(claims)                    // add custom claims like roles
                .signWith(key, SignatureAlgorithm.HS256) // sign token so it can't be tampered with
                .compact();                           // final token string
    }

    public Jws<Claims> parseAndValidate(String token) {
        // parserBuilder + setSigningKey verifies signature and expiration
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public String extractUsername(String token) {
        return parseAndValidate(token).getBody().getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Object rolesObj = parseAndValidate(token).getBody().get("roles");
        if (rolesObj instanceof List<?> list) {
            List<String> roles = new ArrayList<>();
            for (Object r : list) roles.add(String.valueOf(r));
            return roles;
        }
        return List.of();
    }
}
