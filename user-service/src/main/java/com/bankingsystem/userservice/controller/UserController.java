package com.bankingsystem.userservice.controller;

import com.bankingsystem.userservice.model.User;
import com.bankingsystem.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    ResponseEntity<Optional<User>> findByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    ResponseEntity<User> save(@RequestBody User user) {
        userService.CreateUser(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    ResponseEntity<Optional<User>> findById(@PathVariable Long id) {
        Optional<User> user = userService.getUserByUserId(id);
        return ResponseEntity.ok(user);
    }
}
