package com.bankingsystem.userservice.controller;

import com.bankingsystem.userservice.model.User;
import com.bankingsystem.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    ResponseEntity<User> findByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    ResponseEntity<User> save(@RequestBody User user) {
        userService.CreateUser(user);
        return ResponseEntity.ok(user);
    }
}
