package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.dto.ProductDTO;
import com.ngocnguyen.jewelry_ecommerce.entity.Category;
import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import com.ngocnguyen.jewelry_ecommerce.entity.User;
import com.ngocnguyen.jewelry_ecommerce.repository.ProductTableRepository;
import com.ngocnguyen.jewelry_ecommerce.service.CategoryService;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import com.ngocnguyen.jewelry_ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
//@RequiredArgsConstructor
public class APIController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @ModelAttribute("cates")
    public List<Category> categories(){
        return categoryService.getAllCate();
    }
    @ModelAttribute("genders")
    public List<String> allGender(){
        return Arrays.asList("Nam", "Nữ", "Cho bé", "Cặp đôi");
    }

    @RequestMapping(value = "/api/products", method = RequestMethod.GET)
    public DataTablesOutput<ProductDTO> getProduct(@Valid DataTablesInput input){
        return productService.getAllProductDTO(input);
    }
    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public DataTablesOutput<User> getUsers(@Valid DataTablesInput input){
        return userService.getAllUser(input);
    }
}
