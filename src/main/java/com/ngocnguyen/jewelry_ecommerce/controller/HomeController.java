package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.Category;
import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import com.ngocnguyen.jewelry_ecommerce.entity.Rate;
import com.ngocnguyen.jewelry_ecommerce.service.CartService;
import com.ngocnguyen.jewelry_ecommerce.service.CategoryService;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import com.ngocnguyen.jewelry_ecommerce.service.RateService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartService cartService;
    @Autowired
    private RateService rateService;

    @ModelAttribute("countCart")
    public int countCart() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return cartService.getCart().count();
        } else {
            return 0;
        }
    }
    @ModelAttribute("cates")
    public List<Category> cates(){
        return categoryService.getAllCate();
    }
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
    @RequestMapping("/home")
    public String home(Model model) throws Exception {
//        model.addAttribute("cates", categoryService.getAllCate());
        model.addAttribute("topSale", productService.topSaleProduct(10));
        List<Category> topCateSale = categoryService.topCateSale(2);
        model.addAttribute("topCateSale", topCateSale);
        model.addAttribute("newest", productService.newestProduct(6));
        return "/client/index";
    }
    @RequestMapping("/search")
    public String search(Model model, @Param("keyword") String keyword) throws Exception {
        List<Product> results = productService.searchResult(keyword);
        model.addAttribute("products", results);
        return "/client/product";
    }
    @RequestMapping("/products")
    public String allProduct(Model model){
        model.addAttribute("products", productService.findAll());
        return "/client/product";
    }

    @RequestMapping("/details")
    public String details(@RequestParam("id") Long id, Model model) throws Exception {
        Optional<Product> product = productService.findById(id);
        if(product.isPresent()){
            model.addAttribute("product", product.get());
            model.addAttribute("averageStar", rateService.getAverageRate(id));
            model.addAttribute("images", product.get().getImages().split(","));
            model.addAttribute("rates", rateService.getAllRate());
        } else {
            model.addAttribute("product", null);
        }
        if(!rateService.isGuest()){
            model.addAttribute("rateExisted", rateService.isExisted(id));
        }
        if(!rateService.isGuest() && !rateService.isExisted(id)){
            Rate rate = rateService.getRate(id);
            rate.setProduct(product.get());
            model.addAttribute("form", rate);
        }
        return "/client/product-details";
    }
    @PostMapping("/rate")
    public String rate(@ModelAttribute("form") Rate form){
        rateService.saveRate(form);
//        String referrer = request.getHeader("referer");
        return "redirect:/details?id=" + form.getProduct().getId();
    }
}
