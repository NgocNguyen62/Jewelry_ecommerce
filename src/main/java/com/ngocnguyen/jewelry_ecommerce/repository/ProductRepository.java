package com.ngocnguyen.jewelry_ecommerce.repository;

import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p from Product p where p.productName like %?1%"
    + " or p.category.categoryName like %?1%"
            + "or p.gender like %?1%"
            + "OR CONCAT(p.price, '') LIKE %?1%")
    List<Product> search(String keyword);
}
