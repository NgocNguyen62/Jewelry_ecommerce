package com.example.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String role;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column
    private LocalDate birthday;
    @Column
    private String phone;
    @Column
    private String gender;
    @Column
    private String address;
    @Column(name = "user_status")
    private int userStatus;
    private LocalDateTime create_at;
    private String create_by;
    private LocalDateTime update_at;
    private String update_by;
}
