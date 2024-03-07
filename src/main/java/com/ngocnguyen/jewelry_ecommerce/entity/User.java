package com.ngocnguyen.jewelry_ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "birthday")
    private LocalDate birthday;
    @Column(name = "phone")
    private String phone;
    @Column(name = "gender")
    private String gender;
    @Column(name = "address")
    private String address;
    @Column(name = "user_status")
    private int userStatus;
    @Column(name = "create_at")
    private LocalDateTime createAt;
    @Column(name = "create_by")
    private Long createBy;
    @Column(name = "update_at")
    private LocalDateTime updateAt;
    @Column(name = "update_by")
    private Long updateBy;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Cart cart;
}
