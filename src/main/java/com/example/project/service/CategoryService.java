package com.example.project.service;

import com.example.project.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category saveCategory(Category cate);
    void delete(Long id);
    List<Category> getAllCate();
    Optional<Category> findById(Long id);
}
