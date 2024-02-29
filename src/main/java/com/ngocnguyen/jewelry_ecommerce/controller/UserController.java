package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.User;
import com.ngocnguyen.jewelry_ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @ModelAttribute("roles")
    public List<String> allRole(){
        return Arrays.asList("USER", "ADMIN");
    }
    @ModelAttribute("allStatus")
    public List<Integer> allStatus(){
        return Arrays.asList(1, 0);
    }
    @GetMapping("index")
    public String index(Model model){
        List<User> users = userService.getAllUser();
        model.addAttribute("users", users);
        return "user/index";
    }

    @GetMapping("add")
    public String add(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("editMode", false);
        return "user/add";
    }
    @PostMapping("save")
    public String save(@ModelAttribute User user) throws Exception{
        userService.saveUser(user);
        return "redirect:/user/index";
    }
    @GetMapping("edit")
    public String edit(@RequestParam("id") Long id, Model model){
        Optional<User> userEdit = userService.findUserById(id);
        model.addAttribute("editMode", true);
        userEdit.ifPresent(user -> model.addAttribute("user", user));
        userEdit.orElseThrow();
        return "user/edit";
    }
    @GetMapping("delete")
    public String deleteUser(@RequestParam("id") Long id, Model model) throws Exception {
        try {
            userService.deleteUser(id);
        } catch (Exception e){
            throw new Exception(e);
        }
        return "redirect:/user/index";
    }
}
