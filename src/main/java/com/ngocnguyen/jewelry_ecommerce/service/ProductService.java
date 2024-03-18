package com.ngocnguyen.jewelry_ecommerce.service;

import com.ngocnguyen.jewelry_ecommerce.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();
    Product create(Product product) throws Exception;
    Optional<Product> findById(Long id);
    void delete(Long id) throws Exception;

    int countProducts();

    void updateQuantity(Long id, int quantity) throws Exception;

    void updateSales(Long id, int quantity) throws Exception;

    int getAllSales();

    List<Product> topSaleProduct(int limit);

    int[] countInTopSales(int maxTop);

    String[] nameOfTopSales(int maxTop);

    List<Product> searchResult(String keyword);

    List<Product> newestProduct(int limit);
}
