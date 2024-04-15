package com.ngocnguyen.jewelry_ecommerce.dto;

import com.ngocnguyen.jewelry_ecommerce.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private  Long id;
    private String productName;
    private String gender;
    private String avatar;
    private String images;
    private String description;
    private double price;
    private double discount;
    private int quantity;
    private int productStatus;
    private int sales;
    private Category category;
}
