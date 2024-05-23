package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.User;
import com.ngocnguyen.jewelry_ecommerce.service.UserService;
import com.ngocnguyen.jewelry_ecommerce.utils.UserExport;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
    @GetMapping("export")
    public void export(HttpServletResponse response) throws IOException{
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=user" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List <User> listOfUsers = userService.getAllUser();
        UserExport generator = new UserExport(listOfUsers);
        generator.generateExcelFile(response);
    }

    @PostMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    public String changePass(@RequestParam("password") String newPass, @RequestParam("oldPass") String oldPass, Model model){
        User currentUser = userService.getCurrentUser();
        if(userService.checkPassword(oldPass, currentUser.getPassword())){
            userService.updatePassword(currentUser, newPass);
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Mật khẩu cũ không đúng.");
            return "redirect:/changePassForm?error";        }

    }
}
