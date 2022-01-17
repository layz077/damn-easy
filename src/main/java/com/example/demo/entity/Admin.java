package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String name;
    @Column(name = "user_name")
    private String userName;
    private String email;
    @NotNull
    private String password;
    @Column(name="phone_number")
    private String phoneNumber;
    @Column(name="last_login_ip")
    private String lastLoginIp;
    @Column(name="created_on")
    private Date createdOn;
    @Column(name="updated_on")
    private Date updatedOn;
    @Column(name="created_by")
    private long createdBy;
}
