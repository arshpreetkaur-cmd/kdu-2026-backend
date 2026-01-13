package org.example.quickshiplogisticshub.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import static org.example.quickshiplogisticshub.constants.AppConstants.*;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // MUST be 32+ characters (256 bits for HS256)
    private static final String SECRET =
            "quickship-secure-jwt-secret-key-123456";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )
                .signWith(key)
                .compact();
    }

    public Claims extractClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
