package com.ngocnguyen.jewelry_ecommerce.service;

import com.ngocnguyen.jewelry_ecommerce.entity.Rate;

import java.util.List;

public interface RateService {
//    Rate saveRate(Long productId, Rate form);

//    Rate saveRate(Rate rate, Rate form) throws Exception;

    Rate saveRate(Rate form);

    boolean isExisted(Long productId) throws Exception;

    List<Rate> getAllRate();

    boolean isGuest();

    Rate getRate(Long productId);

    double getAverageRate(Long productId);

    int countRate(Long productId);

    List<Rate> getAllRateByProduct(Long id);
}
