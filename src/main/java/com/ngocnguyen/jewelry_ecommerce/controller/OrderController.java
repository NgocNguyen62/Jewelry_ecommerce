package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.Category;
import com.ngocnguyen.jewelry_ecommerce.entity.Order;
import com.ngocnguyen.jewelry_ecommerce.service.CartService;
import com.ngocnguyen.jewelry_ecommerce.service.CategoryService;
import com.ngocnguyen.jewelry_ecommerce.service.FavoriteService;
import com.ngocnguyen.jewelry_ecommerce.service.OrderService;
import com.ngocnguyen.jewelry_ecommerce.service.UserService;
import com.ngocnguyen.jewelry_ecommerce.utils.CommonConstants;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;
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
    @ModelAttribute("shipping")
    public double shipping(){
        return CommonConstants.SHIPPING;
    }
    @ModelAttribute("countFavorite")
    public int countFavorite(){
        return favoriteService.count();
    }
    @ModelAttribute("cates")
    public List<Category> cates(){
        return categoryService.getAllCate();
    }

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
        model.addAttribute("deliverOrders", orderService.getDeliveringOrder());
        model.addAttribute("waitOrders", orderService.getWaitingOrder());
        model.addAttribute("history", orderService.history());
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
        model.addAttribute("shipping", CommonConstants.SHIPPING);
        model.addAttribute("total", orderService.getOrder(id).get().getTotalPrice() + CommonConstants.SHIPPING);
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
    @PostMapping("requestCancel")
    public String requestCancel(@RequestParam("id") Long id, @RequestParam("reason") String reason){
        orderService.requestCancel(id, reason);
        return "redirect:/order/index";
    }

    @GetMapping("/confirmRequest")
    public String confirmOrder(@RequestParam("id") Long id){
        orderService.confirmOrderRequest(id);
        return "redirect:/order/manager";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/manager")
    public String manager(Model model){
        model.addAttribute("confirmRequest", orderService.getWaitConfirm());
        model.addAttribute("cancelRequest", orderService.getCancelRequest());
        return "/order/manager";
    }
}
