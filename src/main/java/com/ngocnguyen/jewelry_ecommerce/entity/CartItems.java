package com.ngocnguyen.jewelry_ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cart_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @Column(name = "cart_id")
//    private Long cart_id;

//    @Column(name = "product_id")
//    private Long product_id;

    @Column(name = "quantity")
    private int quantity = 1;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    public double getTotalPrice(){
        return this.product.getDiscount() * this.getQuantity();
    }

}
