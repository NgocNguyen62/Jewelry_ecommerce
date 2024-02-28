package com.example.project.service.implement;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getId() == null) {
            user.setCreate_at(LocalDateTime.now());
            user.setCreate_by(authentication.getName());
        } else {
            Optional<User> temp = userRepository.findById(user.getId());
            user.setCreate_at(temp.get().getCreate_at());
            user.setCreate_by(temp.get().getCreate_by());
            user.setUpdate_at(LocalDateTime.now());
            user.setUpdate_by(authentication.getName());
        }
        if(userRepository.existsByEmail(user.getEmail()) && !userRepository.existsById(user.getId())){
           throw new Exception("Email đã tồn tại") ;
        }
        return userRepository.save(user);
    }
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
}
