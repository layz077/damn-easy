package com.example.demo.entity;

import java.sql.Date;

import javax.persistence.Entity;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationClass {

	private String phonenumber;
	private String password;
	private boolean enabled;
	private String role;
	
}
