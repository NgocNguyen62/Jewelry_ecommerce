package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.Category;
import com.ngocnguyen.jewelry_ecommerce.service.CartService;
import com.ngocnguyen.jewelry_ecommerce.service.CategoryService;
import com.ngocnguyen.jewelry_ecommerce.service.FavoriteService;
import com.ngocnguyen.jewelry_ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @ModelAttribute("countCart")
    public int countCart() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return cartService.getCart().count();
        } else {
            return 0;
        }
    }
    @ModelAttribute("countFavorite")
    public int countFavorite(){
        return favoriteService.count();
    }
    @ModelAttribute("cates")
    public List<Category> cates(){
        return categoryService.getAllCate();
    }

    @Autowired
    private HttpServletRequest request;
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/favoriteList")
    public String favoriteList(Model model){
        model.addAttribute("items", favoriteService.getAllFavorite());
        return "/favorite/index";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add")
    public String add(@RequestParam("id") Long id) throws Exception {
        favoriteService.addFavorite(id);
        String referrer = request.getHeader("referer");
        if (referrer != null && !referrer.isEmpty()) {
            return "redirect:" + referrer;
        } else {
            return "redirect:/home";
        }
    }
    @GetMapping("/remove")
    public String remove(@RequestParam("id") Long id) throws Exception {
        favoriteService.remove(id);
        String referrer = request.getHeader("referer");
        if (referrer != null && !referrer.isEmpty()) {
            return "redirect:" + referrer;
        } else {
            return "redirect:/home";
        }
    }
}
