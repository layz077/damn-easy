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
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long userId;
    @NotNull
    private String name;
    @Column(name = "username")
    private String userName;
    @Column(name = "email")
    private String email;
    @NotNull
    @Column(name = "password")
    private String password;
    @Column(name="phonenumber")
    private String phoneNumber;
    @Column(name="last_login_ip")
    private String lastLoginIp;
    @Column(name="created_on")
    private Date createdOn;
    @Column(name="updated_on")
    private Date updatedOn;  
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "account_deleted")
    private boolean deleted;
}
