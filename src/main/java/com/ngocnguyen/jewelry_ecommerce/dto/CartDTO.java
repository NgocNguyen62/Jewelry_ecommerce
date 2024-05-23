package com.ngocnguyen.jewelry_ecommerce.dto;

import com.ngocnguyen.jewelry_ecommerce.entity.CartItems;
import com.ngocnguyen.jewelry_ecommerce.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
//    private User user;
    @Autowired
    private List<CartItemDTO> cartItemDTOS ;

    public int count(){
        return cartItemDTOS.size();
    }
    public double getCartTotalPrice(){
        double result = 0;
        for (CartItemDTO i:cartItemDTOS) {
            result += i.getTotalPrice();
        }
        return result;
    }
}
