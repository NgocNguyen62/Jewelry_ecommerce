package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Date birthday;
    private String phone;
    private String gender;
    private String address;
}
