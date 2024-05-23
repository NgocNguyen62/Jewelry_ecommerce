package com.ngocnguyen.jewelry_ecommerce.service;

import com.ngocnguyen.jewelry_ecommerce.component.CustomUserDetails;
import com.ngocnguyen.jewelry_ecommerce.entity.User;
import com.ngocnguyen.jewelry_ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        return user.map(CustomUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("Email không tồn tại"));
    }
}
