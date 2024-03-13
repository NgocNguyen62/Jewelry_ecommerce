package com.ngocnguyen.jewelry_ecommerce.service.implement;

import com.ngocnguyen.jewelry_ecommerce.component.CustomUserDetails;
import com.ngocnguyen.jewelry_ecommerce.entity.Category;
import com.ngocnguyen.jewelry_ecommerce.repository.CategoryRepository;
import com.ngocnguyen.jewelry_ecommerce.service.CategoryService;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;
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

    @Override
    public int countProduct(Long id) throws Exception {
        Optional<Category> category = findById(id);
        if(category.isPresent()){
            return category.get().getProducts().size();
        } else {
            throw new Exception("Phân loại không tồn tại");
        }

    }

    @Override
    public int[] arrCount() throws Exception {
        ArrayList<Integer> count = new ArrayList<>();
        List<Category> categories = getAllCate();
        for (Category cate : categories){
            count.add(countProduct(cate.getId()));
        }
        int[] arr = count.stream().mapToInt(i -> i).toArray();
        return arr;
    }

    @Override
    public  String[] categoriesName(){
        List<Category> categories = getAllCate();
        return categories.stream()
                .map(Category::getCategoryName)
                .toArray(String[]::new);
    }
    @Override
    public double[] percentProduct() throws Exception {
        int allProducts = productService.countProducts();
        int[] productInCate = arrCount();
        double[] percent = new double[productInCate.length];
        for(int i = 0; i < percent.length; i++){
            percent[i] = (double) productInCate[i] / allProducts * 100;
        }
        return percent;
    }
}
