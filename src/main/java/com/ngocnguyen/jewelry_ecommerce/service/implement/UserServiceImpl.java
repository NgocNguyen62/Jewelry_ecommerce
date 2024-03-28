package com.ngocnguyen.jewelry_ecommerce.service.implement;

import com.ngocnguyen.jewelry_ecommerce.component.CustomUserDetails;
import com.ngocnguyen.jewelry_ecommerce.entity.Cart;
import com.ngocnguyen.jewelry_ecommerce.entity.Favorite;
import com.ngocnguyen.jewelry_ecommerce.entity.User;
import com.ngocnguyen.jewelry_ecommerce.repository.CartRepository;
import com.ngocnguyen.jewelry_ecommerce.repository.FavoriteRepository;
import com.ngocnguyen.jewelry_ecommerce.repository.UserRepository;
import com.ngocnguyen.jewelry_ecommerce.service.CartService;
import com.ngocnguyen.jewelry_ecommerce.service.FavoriteService;
import com.ngocnguyen.jewelry_ecommerce.service.UserService;
import com.ngocnguyen.jewelry_ecommerce.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private CartRepository cartRepository;
    @Autowired private FavoriteRepository favoriteRepository;
    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (user.getId() == null) {
                user.setCreateAt(LocalDateTime.now());
                user.setCreateBy(currentUser.getUserId());
            } else {
                Optional<User> temp = userRepository.findById(user.getId());
                if(temp.isPresent()){
                    user.setCreateAt(temp.get().getCreateAt());
                    user.setCreateBy(temp.get().getCreateBy());
                    user.setUpdateAt(LocalDateTime.now());
                    user.setUpdateBy(currentUser.getUserId());
                } else {
                    throw new Exception("Không tìm thấy người dùng hiện tại");
                }
            }
        } else {
            throw new Exception("Chưa đăng nhập");
        }

        if(userRepository.existsByEmail(user.getEmail()) && !userRepository.existsById(user.getId())){
           throw new Exception("Email đã tồn tại") ;
        }
        userRepository.save(user);
        if(cartRepository.findByUserId(user.getId())== null){
            Cart newCart = new Cart();
            newCart.setUser(user);
            cartRepository.save(newCart);
        }
        if(favoriteRepository.findByUser_Id(user.getId())== null){
            Favorite newFavorite = new Favorite();
            newFavorite.setUser(user);
            favoriteRepository.save(newFavorite);
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

    @Override
    public User register(User user){
        user.setRole("USER");
        user.setUserStatus(CommonConstants.UNLOCK_STATUS);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        Cart newCart = new Cart();
        newCart.setUser(user);
        cartRepository.save(newCart);
        Favorite newFavorite = new Favorite();
        newFavorite.setUser(user);
        favoriteRepository.save(newFavorite);
        return userRepository.save(user);
    }
    @Override
    public User updateAccount(User user){
        user.setRole("USER");
        user.setUserStatus(CommonConstants.UNLOCK_STATUS);
        return userRepository.save(user);
    }
    @Override
    public User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            CustomUserDetails currentUserDetail = (CustomUserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findById(currentUserDetail.getUserId()).get();
            return currentUser;
        } else {
            return null;
        }
    }
    @Override
    public boolean checkPassword(String oldPass, String pass){
        return passwordEncoder.matches(oldPass, pass);
    }
    @Override
    public void updatePassword(User user, String newPass){
        user.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(user);
    }
}
