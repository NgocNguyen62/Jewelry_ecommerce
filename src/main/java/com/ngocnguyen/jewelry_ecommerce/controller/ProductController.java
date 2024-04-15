package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.Category;
import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import com.ngocnguyen.jewelry_ecommerce.service.CategoryService;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import com.ngocnguyen.jewelry_ecommerce.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("product")
public class ProductController {
//    Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @ModelAttribute("cates")
    public List<Category> categories(){
        return categoryService.getAllCate();
    }
    @ModelAttribute("genders")
    public List<String> allGender(){
        return Arrays.asList("Nam", "Nữ", "Cho bé", "Cặp đôi");
    }

//    @Autowired
//    private ProductTableRepository productTableRepository;
//    @GetMapping("index")
//    public String listProducts(Model model,
//                               @RequestParam("page") Optional<Integer> page,
//                               @RequestParam("size") Optional<Integer> size
//    ){
//        int currentPage = page.orElse(1);
//        int pageSize = size.orElse(CommonConstants.SIZE_OF_PAGE_INDEX);
//        Page<Product> productPage = productService.getAllProducts(PageRequest.of(currentPage - 1, pageSize));
//        model.addAttribute("productPage", productPage);
//        int totalPages = productPage.getTotalPages();
//        if(totalPages > 0){
//            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
//                    .boxed()
//                    .collect(Collectors.toList());
//            model.addAttribute("pageNumbers", pageNumbers);
//        }
//        return "/product/index";
//    }

    @GetMapping("index")
    public String getAllProduct(){
        return "/product/product";
    }

    @GetMapping("add")
    @PreAuthorize("isAuthenticated()")
    public String addProduct(Model model){
        model.addAttribute("editMode", true);
        Product product = new Product();
        model.addAttribute("product", product);
        return "product/add";
    }
    /**
     * Save product to DB
     * @param product
     * @return product/index
     */
    @PostMapping("save")
    public String save(@ModelAttribute("product") Product product) throws Exception {
        try {
//            System.out.println(product);
            productService.create(product);
        } catch (Exception e){
            throw new Exception(e);
        }
        return "redirect:/product/index";
    }

    @GetMapping("edit")
    @PreAuthorize("isAuthenticated()")
    public String edit(@RequestParam("id") Long id, Model model){
        Optional<Product> productEdit = productService.findById(id);
        model.addAttribute("editMode", true);
        productEdit.ifPresent(product->model.addAttribute("product", productEdit));
        productEdit.orElseThrow();
        return "product/edit";
    }

    /**
     * Delete in DB
     * @param id
     * @return product/index
     * @throws Exception
     */
    @GetMapping("delete")
    public String delete(@RequestParam("id") Long id) throws Exception {
        try {
            productService.delete(id);
        } catch (Exception e){
            throw new Exception(e);
        }
        return "redirect:/product/index";
    }
}
