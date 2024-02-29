package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.Category;
import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import com.ngocnguyen.jewelry_ecommerce.service.CategoryService;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @ModelAttribute("cates")
    public List<Category> categories(){
        return categoryService.getAllCate();
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
    @PostMapping("save")
    public String save(@ModelAttribute("product") Product product){
        productService.create(product);
        return "redirect:/product/index";
    }
    @GetMapping("edit")
    public String edit(@RequestParam("id") Long id, Model model){
        Optional<Product> productEdit = productService.findById(id);
        model.addAttribute("editMode", true);
        productEdit.ifPresent(product->model.addAttribute("product", productEdit));
        return "product/edit";
    }
    @GetMapping("delete")
    public String delete(@RequestParam("id") Long id){
        productService.delete(id);
        return "redirect:/product/index";
    }
}
