package org.example.securecorporatetalentportal.user;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//role based access
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    //BASIC and ADMIN can view
    @GetMapping
    @PreAuthorize("hasAnyRole('BASIC', 'ADMIN')")
    public List<User> listUsers() {
        return userService.getStore().findAll().stream()
                .map(u-> new User(u.getUserName(), "***", u.getEmail(), u.getRoles()))
                .toList();
    }

    //Only ADMIN can add
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUser(@Valid @RequestBody CreateUserRequest req) {

        //if username already exists
        if(userService.getStore().exists(req.getUserName())){

        }

        userService.register(req.getUserName(), req.getPassword(), req.getEmail(), req.getRoles());
        return ResponseEntity.ok(Map.of("message", "User Created"));
    }
}
