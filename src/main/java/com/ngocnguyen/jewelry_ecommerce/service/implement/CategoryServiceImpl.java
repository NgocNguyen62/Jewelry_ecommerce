package com.ngocnguyen.jewelry_ecommerce.service.implement;

import com.ngocnguyen.jewelry_ecommerce.component.CustomUserDetails;
import com.ngocnguyen.jewelry_ecommerce.entity.Category;
import com.ngocnguyen.jewelry_ecommerce.repository.CategoryRepository;
import com.ngocnguyen.jewelry_ecommerce.service.CategoryService;
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
    private CategoryRepository categoryRepository;
    @Override
    public Category saveCategory(Category cate) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
            if (cate.getId() == null) {
                cate.setCreateAt(LocalDateTime.now());
                cate.setCreateBy(currentUser.getUserId());
            } else {
                Optional<Category> temp = categoryRepository.findById(cate.getId());
                if(temp.isPresent()){
                    cate.setCreateAt(temp.get().getCreateAt());
                    cate.setCreateBy(temp.get().getCreateBy());
                    cate.setUpdateAt(LocalDateTime.now());
                    cate.setUpdateBy(currentUser.getUserId());
                }
                temp.orElseThrow();
            }
        } else {
            throw new Exception("Chưa đăng nhập");
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
