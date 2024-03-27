package com.ngocnguyen.jewelry_ecommerce.controller;

import com.ngocnguyen.jewelry_ecommerce.entity.Category;
import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import com.ngocnguyen.jewelry_ecommerce.entity.Rate;
import com.ngocnguyen.jewelry_ecommerce.entity.User;
import com.ngocnguyen.jewelry_ecommerce.service.CartService;
import com.ngocnguyen.jewelry_ecommerce.service.CategoryService;
import com.ngocnguyen.jewelry_ecommerce.service.FavoriteService;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import com.ngocnguyen.jewelry_ecommerce.service.RateService;
import com.ngocnguyen.jewelry_ecommerce.service.UserService;
import com.ngocnguyen.jewelry_ecommerce.utils.CommonConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartService cartService;
    @Autowired
    private RateService rateService;
    @Autowired
    private UserService userService;
    @Autowired
    private FavoriteService favoriteService;

    @ModelAttribute("currentUrl")
    public String getCurrentUrl(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("countCart")
    public int countCart() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return cartService.getCart().count();
        } else {
            return 0;
        }
    }
    @ModelAttribute("countFavorite")
    public int countFavorite(){
        return favoriteService.count();
    }
    @ModelAttribute("cates")
    public List<Category> cates(){
        return categoryService.getAllCate();
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("editMode", false);
        return "/client/register";
    }
    @RequestMapping("/myAccount")
    public String myAccount(Model model){
        User userEdit = userService.getCurrentUser();
        model.addAttribute("editMode", true);
        model.addAttribute("user", userEdit);
        return "client/myAccount";
    }
    @PostMapping("/createAccount")
    public String createAccount(@ModelAttribute("user") User user){
        userService.register(user);
        return "redirect:/home";
    }
    @PostMapping("/updateAccount")
    public String updateAccount(@ModelAttribute("user") User user){
        userService.updateAccount(user);
        return "/client/myAccount";
    }
    @RequestMapping("/dashboard")
    public String dashboard(Model model) throws Exception {
        model.addAttribute("label", categoryService.categoriesName());
        model.addAttribute("data", categoryService.percentProduct());

        model.addAttribute("labelTopSale", productService.nameOfTopSales(10));
        model.addAttribute("dataTopSale", productService.countInTopSales(10));
        return "/dashboard";
    }
    @RequestMapping("/home")
    public String home(Model model) throws Exception {
        model.addAttribute("topSale", productService.topSaleProduct(10));
        List<Category> topCateSale = categoryService.topCateSale(CommonConstants.NUM_TOP_SALE);
        model.addAttribute("topCateSale", topCateSale);
        model.addAttribute("newest", productService.newestProduct(CommonConstants.NUM_NEWEST_PRODUCTS));
        return "/client/index2";
    }
    @RequestMapping("/search")
    public String search(Model model, @Param("keyword") String keyword) throws Exception {
        List<Product> results = productService.searchResult(keyword);
        model.addAttribute("products", results);
        return "/client/product";
    }
    @RequestMapping("/products")
    public String listProducts(Model model,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size
                               ){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(CommonConstants.SIZE_OF_PAGE);
        Page<Product> productPage = productService.getAllProducts(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("productPage", productPage);
        int totalPages = productPage.getTotalPages();
        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "/client/product";
    }

    @RequestMapping("/details")
    public String details(@RequestParam("id") Long id, Model model) throws Exception {
        Optional<Product> product = productService.findById(id);
        if(product.isPresent()){
            model.addAttribute("product", product.get());
            model.addAttribute("averageStar", rateService.getAverageRate(id));
            model.addAttribute("images", product.get().getImages().split(","));
            model.addAttribute("rates", rateService.getAllRateByProduct(id));
            model.addAttribute("countRate", rateService.countRate(id));
            model.addAttribute("relatives", productService.getRelativeProduct(id));
            model.addAttribute("isFavorite", favoriteService.isInFavorite(id));
        } else {
            model.addAttribute("product", null);
        }
        if(!rateService.isGuest()){
            model.addAttribute("rateExisted", rateService.isExisted(id));
        }
        if(!rateService.isGuest() && !rateService.isExisted(id)){
            Rate rate = rateService.getRate(id);
            rate.setProduct(product.get());
            model.addAttribute("form", rate);
        }
        return "/client/detail";
    }
    @PostMapping("/rate")
    public String rate(@ModelAttribute("form") Rate form){
        rateService.saveRate(form);
        return "redirect:/details?id=" + form.getProduct().getId();
    }
}
