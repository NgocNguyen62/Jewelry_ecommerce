package com.ngocnguyen.jewelry_ecommerce.service;

import com.ngocnguyen.jewelry_ecommerce.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category saveCategory(Category cate) throws Exception;
    void delete(Long id);
    List<Category> getAllCate();
    Optional<Category> findById(Long id);
}
