package org.example.quickshiplogisticshub.controller;

import org.example.quickshiplogisticshub.model.User;
import org.example.quickshiplogisticshub.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // passwords encoded ONCE at startup
    private final List<User> users;

    public AuthController(JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;

        this.users = List.of(
                new User(
                        "manager",
                        passwordEncoder.encode("1234"),
                        "MANAGER"
                ),
                new User(
                        "driver",
                        passwordEncoder.encode("1234"),
                        "DRIVER"
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {

        for (User user : users) {

            if (user.getUsername().equals(request.getUsername())
                    && passwordEncoder.matches(
                    request.getPassword(),
                    user.getPassword()
            )) {

                String token = jwtUtil.generateToken(
                        user.getUsername(),
                        user.getRole()
                );

                return ResponseEntity.ok(Map.of("token", token));
            }
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }
}