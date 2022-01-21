package com.example.demo.repository;

import com.example.demo.entity.User;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	@Query(value = "SELECT email FROM user WHERE user_id=?1",nativeQuery = true)
	String getEmail(long id);
	
	@Query(value = "SELECT password FROM user WHERE user_id=?1",nativeQuery = true)
	String getHash(long id);
	
	@Query(value = "SELECT * FROM user WHERE phonenumber=?1", nativeQuery = true)
	String getByPhone(String phoneNumber);
	
	@Query(value = "SELECT * FROM user WHERE email=?1", nativeQuery = true)
	String getByEmail(String email);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE user set last_login_ip=?1 WHERE user_id=?2", nativeQuery = true)
	void setLastIp(String ip,long id);
	
	@Query(value = "select * from user where phonenumber=?1", nativeQuery = true)
	User findByUserName(@Param("username") String phonenumber);

}
