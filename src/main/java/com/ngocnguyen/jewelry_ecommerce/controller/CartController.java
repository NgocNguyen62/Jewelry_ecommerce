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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private FavoriteService favoriteService;
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("index")
    public String index(Model model) throws Exception {
        model.addAttribute("items", cartService.getAllCartItems());
        model.addAttribute("cart", cartService.getCart());
        return "/cart/cart";
    }

    /**
     *
     * @param id - Product id
     * @return index
     * @throws Exception
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("add")
    public String add(@RequestParam("id") Long id, HttpServletRequest request) throws Exception {
        String referer = request.getHeader("referer");
        cartService.addItem(id);

        return "redirect:" + referer;
    }

    /**
     *
     * @param id - product id
     * @param quantity (-1, 1)
     * @return index
     * @throws Exception
     */
    @GetMapping("update")
    public String update(@RequestParam("id") Long id, int quantity, RedirectAttributes redirectAttributes) throws Exception {
        try {
            cartService.updateQuantity(id, quantity);
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("outOfStockMessage", e.getMessage());
        }

        return "redirect:/cart/index";
    }

    /**
     *
     * @param id - Product id
     * @return index
     * @throws Exception
     */
    @GetMapping("remove")
    public String remove(@RequestParam("id") Long id) throws Exception {
        cartService.deleteItem(id);
        return "redirect:/cart/index";
    }
}
