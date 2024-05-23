package com.ngocnguyen.jewelry_ecommerce.repository;

import com.ngocnguyen.jewelry_ecommerce.entity.FavoriteItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItems, Long> {
    Optional<FavoriteItems> findByFavorite_IdAndProduct_Id(Long favoriteId, Long productId);
    boolean existsByFavorite_IdAndProduct_Id(Long favoriteId, Long productId);
}
