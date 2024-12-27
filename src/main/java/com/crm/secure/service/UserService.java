package com.crm.secure.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.crm.secure.model.User;
import com.crm.secure.repository.UserRepository;
import org.springframework.security.core.Authentication;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String verify(User userDetail) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetail.getUsername(), userDetail.getPassword()));
        if (authentication.isAuthenticated()) {
            return "success";
        } else {
            return "fail";
        }
    }

}