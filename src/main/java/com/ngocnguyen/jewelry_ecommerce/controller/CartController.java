package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @GetMapping("index")
    public String index(Model model) throws Exception {
        model.addAttribute("items", cartService.getAllCartItems());
        model.addAttribute("cart", cartService.getCart());
        return "/cart/index";
    }

    /**
     *
     * @param id - Product id
     * @return index
     * @throws Exception
     */
    @GetMapping("add")
    public String add(@RequestParam("id") Long id) throws Exception {
        cartService.addItem(id);
        return "redirect:/cart/index";
    }

    /**
     *
     * @param id - product id
     * @param quantity (-1, 1)
     * @return index
     * @throws Exception
     */
    @GetMapping("update")
    public String update(@RequestParam("id") Long id, int quantity) throws Exception {
        cartService.updateQuantity(id, quantity);
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
