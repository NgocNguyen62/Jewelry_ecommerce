package com.ngocnguyen.jewelry_ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "category_description")
    private String description;
    @Column(name = "category_status")
    private int categoryStatus;
    @Column(name = "create_at")
    private LocalDateTime createAt;
    @Column(name = "create_by")
    private Long createBy;
    @Column(name = "update_at")
    private LocalDateTime updateAt;
    @Column(name = "update_by")
    private Long updateBy;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;
}
