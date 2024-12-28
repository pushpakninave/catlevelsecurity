package com.crm.secure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.secure.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}