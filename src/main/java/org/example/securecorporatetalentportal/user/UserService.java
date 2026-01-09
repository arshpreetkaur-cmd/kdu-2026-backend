package org.example.securecorporatetalentportal.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

//Bcrypt encoding happens here
@Service
public class UserService {

    private final InMemoryUserStore store;
    private final PasswordEncoder passwordEncoder;

    public UserService(InMemoryUserStore store, PasswordEncoder passwordEncoder) {
        this.store = store;
        this.passwordEncoder = passwordEncoder;

        //seed two demo users
        register("basic1", "basic123", "basic1@corp.com", List.of("ROLE_BASIC"));
        register("admin1", "admin123", "admin1@corp.com", List.of("ROLE_ADMIN"));
    }

    public User register(String username, String rawPassword, String email, List<String> roles) {
        // BCrypt hashing happens here
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username, hashedPassword, email, roles);
        store.save(user);
        return user;
    }

    public InMemoryUserStore getStore() {
        return store;
    }
}

