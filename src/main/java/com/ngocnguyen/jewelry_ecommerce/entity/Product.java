package com.ngocnguyen.jewelry_ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "product_name")
    private String productName;
    @Column(name = "gender")
    private String gender;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "images")
    private String images;
    @Column(name = "product_description")
    private String description;
    @Column(name = "price")
    private double price;
    @Column(name = "discount")
    private double discount;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "product_status")
    private int productStatus;
    @Column(name = "sales")
    private int sales;
    @Column(name = "create_at")
    private LocalDateTime createAt;
    @Column(name = "create_by")
    private Long createBy;
    @Column(name = "update_at")
    private LocalDateTime updateAt;
    @Column(name = "update_by")
    private Long updateBy;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

//    @ManyToMany(mappedBy = "products", cascade = CascadeType.ALL)
//    private List<CartItems> cartItems;

    @Transient
    private MultipartFile file;
    @Transient
    private MultipartFile[] files;
}
