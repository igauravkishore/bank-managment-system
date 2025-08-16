package com.bankingsystem.userservice.service;

import com.bankingsystem.userservice.model.User;
import com.bankingsystem.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void CreateUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId).get();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }
}
