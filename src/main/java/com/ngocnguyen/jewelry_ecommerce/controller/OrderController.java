package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.Order;
import com.ngocnguyen.jewelry_ecommerce.service.CartService;
import com.ngocnguyen.jewelry_ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;

    @GetMapping("preview")
    public String preview(Model model) throws Exception {
        try {
            Order order = orderService.preview();
            model.addAttribute("order", order);
            model.addAttribute("items", cartService.getAllCartItems());
            return "/order/form";
        } catch (Exception ex) {
            String encodeMessage = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            return "redirect:error?message=" + encodeMessage;
        }
    }

    @PostMapping ("order")
    public String order(@ModelAttribute("order")Order order) throws Exception {
        orderService.createOrder(order);
        return "redirect:/order/index";
    }

    @GetMapping("index")
    public String index(Model model) throws Exception {
        model.addAttribute("orders", orderService.getDeliveringOrder());
        return "/order/index";
    }

    @GetMapping("history")
    public String history(Model model) throws Exception {
        model.addAttribute("orders", orderService.history());
        return "/order/history";
    }

    @GetMapping("details")
    public String details(@RequestParam("id") Long id, Model model){
        model.addAttribute("items", orderService.getItemsInOrder(id));
        model.addAttribute("order", orderService.getOrder(id));
        return "/order/details";
    }
    @GetMapping("confirm")
    public String confirm(@RequestParam("id") Long id){
        orderService.confirmOrder(id);
        return "redirect:/order/index";
    }
    @GetMapping("cancel")
    public String cancel(@RequestParam("id") Long id) throws Exception {
        orderService.cancelOrder(id);
        return "redirect:/order/index";
    }
}
