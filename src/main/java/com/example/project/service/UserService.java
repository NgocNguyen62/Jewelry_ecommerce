package com.example.project.service;

import com.example.project.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> getAllUser();
    User saveUser(User user) throws Exception;
    void deleteUser(Long id);
    Optional<User> findUserById(Long id);
}
