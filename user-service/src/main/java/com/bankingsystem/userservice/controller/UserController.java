package com.bankingsystem.userservice.controller;

import com.bankingsystem.userservice.model.User;
import com.bankingsystem.userservice.repository.UserRepository;
import com.bankingsystem.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        userService.getUserByUsername(username);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    ResponseEntity<User> save(@RequestBody User user) {
        userService.CreateUser(user);
        return ResponseEntity.ok().build();
    }
}
