package com.ngocnguyen.jewelry_ecommerce.service;

import com.ngocnguyen.jewelry_ecommerce.entity.Product;

import java.util.List;

public interface FavoriteService {
    void addFavorite(Long productId) throws Exception;

    void remove(Long productId) throws Exception;

    List<Product> getAllFavorite();

    boolean isInFavorite(Long productId);

    int count();
}
