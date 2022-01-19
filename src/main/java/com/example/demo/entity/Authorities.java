package com.example.demo.entity;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.management.relation.Role;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_roles")
public class Authorities {

	@Id
	@Column(name="username")
	private String username;
	@Column(name="rolename")
	private String roleName;
	
}
