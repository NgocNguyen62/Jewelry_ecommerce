package com.example.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime create_at;
    private String create_by;
    private LocalDateTime update_at;
    private String update_by;
}
