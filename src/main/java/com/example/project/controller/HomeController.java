package com.example.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/login")
//    public String index(){
//        return "Index";
//    }
//    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
