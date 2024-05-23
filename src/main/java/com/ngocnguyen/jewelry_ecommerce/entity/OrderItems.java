package com.ngocnguyen.jewelry_ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "product_price")
    private double productPrice;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne()
    @JoinColumn(name = "product_id")
    private Product product;
}
