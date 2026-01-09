package org.example.securecorporatetalentportal.auth;

import org.example.securecorporatetalentportal.jwt.JwtService;
import org.example.securecorporatetalentportal.user.InMemoryUserStore;
import org.example.securecorporatetalentportal.user.User;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final InMemoryUserStore store;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthController(InMemoryUserStore store, PasswordEncoder encoder, JwtService jwtService) {
        this.store = store;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        User user = store.findByUsername(request.getUserName()).orElse(null);

        // If the password entered by user is not correct or the user doesn't exist
        if (user == null || !encoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Unauthorized",
                    "message", "Invalid credentials"
            ));
        }

        //otherwise it will generate a token
        String token = jwtService.generateToken(user.getUserName(), user.getRoles());

        // INFO log for audit trail
        log.info("LOGIN SUCCESS: user='{}' roles={}", user.getUserName(), user.getRoles());

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
