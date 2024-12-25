package com.crm.secure.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.secure.model.User;
import com.crm.secure.repository.UserRepository;

@Service
public class UserService{
    @Autowired
    UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

}