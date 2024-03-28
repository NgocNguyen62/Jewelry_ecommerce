package com.ngocnguyen.jewelry_ecommerce.service.implement;

import com.ngocnguyen.jewelry_ecommerce.entity.PasswordResetToken;
import com.ngocnguyen.jewelry_ecommerce.entity.User;
import com.ngocnguyen.jewelry_ecommerce.repository.PasswordTokenResetRepository;
import com.ngocnguyen.jewelry_ecommerce.repository.UserRepository;
import com.ngocnguyen.jewelry_ecommerce.service.PasswordResetService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {
    @Autowired
    private PasswordTokenResetRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public User getUser(String token) {
        Optional<PasswordResetToken> passwordResetToken = repository.findByToken(token);
        if(passwordResetToken.isPresent()){
            return passwordResetToken.get().getUser();
        }
        passwordResetToken.orElseThrow();
        return null;
    }
    @Override
    public void createToken(String email, String token){
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()){
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setToken(token);
            passwordResetToken.setUser(user.get());
            repository.save(passwordResetToken);
        }
        user.orElseThrow();
    }

    @Override
    @Transactional
    public void deleteToken(String token){
        repository.deleteByToken(token);
    }
}
