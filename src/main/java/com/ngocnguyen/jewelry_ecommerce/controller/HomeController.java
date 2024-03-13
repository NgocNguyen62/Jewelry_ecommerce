package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.service.CategoryService;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/dashboard")
    public String dashboard(Model model) throws Exception {
        model.addAttribute("label", categoryService.categoriesName());
        model.addAttribute("data", categoryService.percentProduct());

        model.addAttribute("labelTopSale", productService.nameOfTopSales(10));
        model.addAttribute("dataTopSale", productService.countInTopSales(10));
        return "/dashboard";
    }
}
