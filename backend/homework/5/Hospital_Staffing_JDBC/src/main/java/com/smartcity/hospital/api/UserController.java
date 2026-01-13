package com.smartcity.hospital.api;

import com.smartcity.hospital.db.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        Boolean loggedIn = (Boolean) body.get("loggedIn");
        String timezone = (String) body.get("timezone");
        String tenantId = (String) body.get("tenantId");

        String id = repository.insertUser(username, loggedIn, timezone, tenantId);
        return ResponseEntity.ok(Map.of("id", id));
    }

    @GetMapping
    public List<Map<String, Object>> getUsers(@RequestParam String tenantId) {
        return repository.findUsersByTenant(tenantId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable String userId,
            @RequestParam String tenantId,
            @RequestBody Map<String, String> body
    ) {
        int updated = repository.updateUser(
                userId,
                tenantId,
                body.get("username"),
                body.get("timezone")
        );

        return ResponseEntity.ok(Map.of("updatedRows", updated));
    }
}
