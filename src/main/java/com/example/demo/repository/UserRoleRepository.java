package com.example.demo.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Authorities;

@Repository
public interface UserRoleRepository extends JpaRepository<Authorities, String> {

	@Query(value = "select rolename from user_roles where phonenumber=?1", nativeQuery = true)
	String getRole(String phonenumber);
}
