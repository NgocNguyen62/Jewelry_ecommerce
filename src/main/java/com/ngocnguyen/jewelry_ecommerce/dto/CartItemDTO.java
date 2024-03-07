package com.ngocnguyen.jewelry_ecommerce.dto;

import com.ngocnguyen.jewelry_ecommerce.entity.Cart;
import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Cart cart;
    private Product product;
    private int quantity;
    public double getTotalPrice(){
        return this.product.getDiscount() * this.getQuantity();
    }
}
