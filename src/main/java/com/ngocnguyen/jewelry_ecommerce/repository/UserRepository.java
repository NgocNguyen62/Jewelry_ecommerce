package com.ngocnguyen.jewelry_ecommerce.repository;

import com.ngocnguyen.jewelry_ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsById(Long id);
}
