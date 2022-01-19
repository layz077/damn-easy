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

	@Query(value = "SELECT email FROM user WHERE id=?1",nativeQuery = true)
	String getEmail(long id);
	
	@Query(value = "SELECT password FROM user WHERE id=?1",nativeQuery = true)
	String getHash(long id);
	
	@Query(value = "SELECT * FROM user WHERE phone_number=?1", nativeQuery = true)
	String getByPhone(String phoneNumber);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE user set last_login_ip=?1 WHERE id=?2", nativeQuery = true)
	void setLastIp(String ip,long id);
	
	@Query(value = "SELECT * FROM user WHERE user_name=?1", nativeQuery = true)
	User loadUserByUserName(@Param("user_name") String username);
}
