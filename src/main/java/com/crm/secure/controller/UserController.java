package com.crm.secure.controller;

import org.springframework.web.bind.annotation.RestController;

import com.crm.secure.model.User;
import com.crm.secure.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        try {
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(users);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> getTest() {
        List<User> users = userService.getAllUsers();
        try {
            return ResponseEntity.ok().body("fufk casdof");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("nothing");
        }
    }
    
}
