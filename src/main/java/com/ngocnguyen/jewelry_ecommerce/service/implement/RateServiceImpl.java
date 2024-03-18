package com.ngocnguyen.jewelry_ecommerce.service.implement;

import com.ngocnguyen.jewelry_ecommerce.component.CustomUserDetails;
import com.ngocnguyen.jewelry_ecommerce.entity.Rate;
import com.ngocnguyen.jewelry_ecommerce.entity.User;
import com.ngocnguyen.jewelry_ecommerce.repository.RateRepository;
import com.ngocnguyen.jewelry_ecommerce.repository.UserRepository;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import com.ngocnguyen.jewelry_ecommerce.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RateServiceImpl implements RateService {
    @Autowired
    private RateRepository rateRepository;
    @Autowired
    ProductService productService;

    @Autowired
    UserRepository userRepository;

    private User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            CustomUserDetails currentUserDetail = (CustomUserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findById(currentUserDetail.getUserId()).get();
            return currentUser;
        } else {
            return null;
        }
    }
    @Override
    public Rate saveRate(Rate form){
//        Rate rate = getRate(productId);
//        if(rate != null){
//            rate.setRateTime(LocalDateTime.now());
//            rate.setProduct(form.getProduct());
//            rate.setUser(getCurrentUser());
//            rate.setStars(form.getStars());
//            rate.setComment(form.getComment());
//            return rateRepository.save(rate);
//        } else {
//            throw new Exception("Cần đăng nhập để đánh giá");
//        }
        form.setRateTime(LocalDateTime.now());
        form.setUser(getCurrentUser());
        return rateRepository.save(form);
    }
    @Override
    public boolean isExisted(Long productId) throws Exception {
            Optional<Rate> rate = rateRepository.findByUser_idAndProduct_id(getCurrentUser().getId(), productId);
            return rate.isPresent();
    }
    @Override
    public List<Rate> getAllRate(){
        return rateRepository.findAll();
    }
    @Override
    public boolean isGuest(){
        if(getCurrentUser() != null){
            return false;
        }
        return true;
    }
    @Override
    public Rate getRate(Long productId){
        if(getCurrentUser() != null) {
            Optional<Rate> rate = rateRepository.findByUser_idAndProduct_id(getCurrentUser().getId(), productId);
            if (rate.isEmpty()) {
                rate = Optional.of(new Rate());
            }
            return rate.get();
        }
        return null;
    }
    @Override
    public double getAverageRate(Long productId){
        List<Rate> rates = rateRepository.findAllByProduct_Id(productId);
        if (rates.isEmpty()) {
            return 0.0;
        }

        int totalStars = 0;
        for (Rate rate : rates) {
            totalStars += rate.getStars();
        }

        double averageRating = (double) totalStars / rates.size();
        return Math.round(averageRating * 10.0) / 10.0;
    }
    @Override
    public int countRate(Long productId){
        List<Rate> rates = rateRepository.findAllByProduct_Id(productId);
        return rates.size();
    }

}
