package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.Rate;
import com.ngocnguyen.jewelry_ecommerce.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RateController {
    @Autowired
    private RateService rateService;

//    @RequestParam("/form")
//    public String form(@ModelAttribute("form") Rate form, Model model){
//        rateService.saveRate(form);
//    }
}
