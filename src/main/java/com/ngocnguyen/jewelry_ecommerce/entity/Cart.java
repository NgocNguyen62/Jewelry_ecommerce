package com.ngocnguyen.jewelry_ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @Column(name = "user_id")
//    private Long user_id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItems> cartItems;

    public double getCartTotalPrice(){
        double result = 0;
        for (CartItems i:cartItems) {
            result += i.getTotalPrice();
        }
        return result;
    }
    public int count(){
        return cartItems.size();
    }
}
