package com.ngocnguyen.jewelry_ecommerce.repository;

import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p from Product p where p.productName LIKE CONCAT('%', :keyword, '%')"
    + " or p.category.categoryName LIKE CONCAT('%', :keyword, '%')"
            + "or p.gender LIKE CONCAT('%', :keyword, '%')"
            + "OR CONCAT(p.price, '') LIKE CONCAT('%', :keyword, '%')")
    List<Product> search(String keyword);
    List<Product> findAllByCategory_Id(Long cateId);
    Page<Product> findAll(Pageable pageable);
    List<Product> findAllByCategory_IdAndIdNot(Long cateId, Long productId);
}
