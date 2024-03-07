package com.ngocnguyen.jewelry_ecommerce.service;

import com.ngocnguyen.jewelry_ecommerce.entity.Cart;
import com.ngocnguyen.jewelry_ecommerce.entity.CartItems;
import java.util.List;
import java.util.Optional;

public interface CartService {
    CartItems addItem (Long productId) throws Exception;
    void deleteItem(Long productId) throws Exception;
    void deleteAll(Long id) throws Exception;
    CartItems updateQuantity(Long id, int quantity) throws Exception;
    List<CartItems> getAllCartItems() throws Exception;
    Optional<Cart> getCart() throws Exception;
}
