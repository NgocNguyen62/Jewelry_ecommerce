package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.Category;
import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import com.ngocnguyen.jewelry_ecommerce.service.CategoryService;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("product")
public class ProductController {
//    Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @ModelAttribute("cates")
    public List<Category> categories(){
        return categoryService.getAllCate();
    }
    @ModelAttribute("genders")
    public List<String> allGender(){
        return Arrays.asList("Nam", "Nữ", "Cho bé", "Cặp đôi");
    }
    @GetMapping("index")
    public String index(Model model){
        model.addAttribute("products", productService.findAll());
        return "/product/index";
    }


    @GetMapping("add")
    public String addProduct(Model model){
        model.addAttribute("editMode", true);
        Product product = new Product();
        model.addAttribute("product", product);
        return "product/add";
    }
    /**
     * Save product to DB
     * @param product
     * @return product/index
     */
    @PostMapping("save")
    public String save(@ModelAttribute("product") Product product) throws Exception {
        try {
//            System.out.println(product);
            productService.create(product);
        } catch (Exception e){
            throw new Exception(e);
        }
        return "redirect:/product/index";
    }

    @GetMapping("edit")
    public String edit(@RequestParam("id") Long id, Model model){
        Optional<Product> productEdit = productService.findById(id);
        model.addAttribute("editMode", true);
        productEdit.ifPresent(product->model.addAttribute("product", productEdit));
        productEdit.orElseThrow();
        return "product/edit";
    }

    /**
     * Delete in DB
     * @param id
     * @return product/index
     * @throws Exception
     */
    @GetMapping("delete")
    public String delete(@RequestParam("id") Long id) throws Exception {
        try {
            productService.delete(id);
        } catch (Exception e){
            throw new Exception(e);
        }
        return "redirect:/product/index";
    }
}
