package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.Category;
import com.ngocnguyen.jewelry_ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("index")
    public String index(Model model){
        List<Category> cates = categoryService.getAllCate();
        model.addAttribute("cates", cates);
        return "category/index";
    }

    @GetMapping("add")
    public String add(Model model){
        model.addAttribute("editMode", false);
        model.addAttribute("cate", new Category());
        return "category/add";
    }

    @GetMapping("edit")
    public String edit(@RequestParam("id") Long id, Model model){
        model.addAttribute("editMode", true);
        Optional<Category> category = categoryService.findById(id);
        category.ifPresent(cate->model.addAttribute("cate", cate));
        category.orElseThrow();
        return "category/edit";
    }

    @PostMapping("save")
    public String save(@ModelAttribute("cate") Category cate) throws Exception {
        try {
            categoryService.saveCategory(cate);
        } catch (Exception e){
            throw new Exception(e);
        }

        return "redirect:/category/index";
    }

    @GetMapping("delete")
    public String deleteCate(@RequestParam("id") Long id) throws Exception {
        try {
            categoryService.delete(id);
        } catch (Exception e){
            throw new Exception(e);
        }
        return "redirect:/category/index";
    }

}
