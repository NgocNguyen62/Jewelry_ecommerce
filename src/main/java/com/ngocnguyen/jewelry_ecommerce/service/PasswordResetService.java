package com.ngocnguyen.jewelry_ecommerce.service;

import com.ngocnguyen.jewelry_ecommerce.entity.User;

public interface PasswordResetService {
    User getUser(String token);

    void createToken(String email, String token);

    void deleteToken(String token);
}
