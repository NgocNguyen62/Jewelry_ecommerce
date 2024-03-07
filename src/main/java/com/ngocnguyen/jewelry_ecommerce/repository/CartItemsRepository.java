package com.ngocnguyen.jewelry_ecommerce.repository;

import com.ngocnguyen.jewelry_ecommerce.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
    Optional<CartItems> findCartItemsByCart_idAndProduct_id(Long cartId, Long productId);
    List<CartItems> findAllByCart_id(Long cartId);
    void deleteCartItemsByCart_id(Long id);
}
