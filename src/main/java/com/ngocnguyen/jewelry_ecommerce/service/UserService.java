package com.ngocnguyen.jewelry_ecommerce.service;

import com.ngocnguyen.jewelry_ecommerce.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> getAllUser();
    User saveUser(User user) throws Exception;
    void deleteUser(Long id);
    Optional<User> findUserById(Long id);

    User register(User user);

    User updateAccount(User user);

    User getCurrentUser();

    boolean checkPassword(String oldPass, String pass);

    void updatePassword(User user, String newPass);
}
