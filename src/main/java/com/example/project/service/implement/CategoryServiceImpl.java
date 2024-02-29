package com.example.project.service.implement;

import com.example.project.entity.Category;
import com.example.project.entity.User;
import com.example.project.repository.CategoryRepository;
import com.example.project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public Category saveCategory(Category cate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (cate.getId() == null) {
            cate.setCreate_at(LocalDateTime.now());
            cate.setCreate_by(authentication.getName());
        } else {
            Optional<Category> temp = categoryRepository.findById(cate.getId());
            cate.setCreate_at(temp.get().getCreate_at());
            cate.setCreate_by(temp.get().getCreate_by());
            cate.setUpdate_at(LocalDateTime.now());
            cate.setUpdate_by(authentication.getName());
        }
        return categoryRepository.save(cate);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> getAllCate() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
}
