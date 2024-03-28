package com.ngocnguyen.jewelry_ecommerce.repository;

import com.ngocnguyen.jewelry_ecommerce.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordTokenResetRepository extends JpaRepository<PasswordResetToken, Long> {
    public Optional<PasswordResetToken> findByToken(String token);
    void deleteByToken(String token);
}
