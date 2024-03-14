package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.Category;
import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import com.ngocnguyen.jewelry_ecommerce.service.CartService;
import com.ngocnguyen.jewelry_ecommerce.service.CategoryService;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartService cartService;
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
    @RequestMapping("/client/home")
    public String home(Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("countCart", cartService.getCart().count());
        } else {
            model.addAttribute("countCart", null);
        }
        model.addAttribute("cates", categoryService.getAllCate());
        model.addAttribute("topSale", productService.topSaleProduct(10));
        List<Category> topCateSale = categoryService.topCateSale(2);
        model.addAttribute("topCateSale", topCateSale);

        return "/client/home";
    }
    @RequestMapping("/search")
    public String search(Model model, @Param("keyword") String keyword){
        List<Product> results = productService.searchResult(keyword);
        model.addAttribute("products", results);
        return "/client/product";
    }
}
