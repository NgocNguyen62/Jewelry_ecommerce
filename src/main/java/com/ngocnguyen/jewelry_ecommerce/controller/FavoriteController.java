package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.service.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/favoriteList")
    public String favoriteList(Model model){
        model.addAttribute("items", favoriteService.getAllFavorite());
        return "/favorite/index";
    }
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
