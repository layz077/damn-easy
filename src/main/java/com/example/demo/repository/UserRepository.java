package com.example.demo.repository;

import com.example.demo.entity.User;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	@Query(value = "SELECT email FROM user WHERE phonenumber=?1",nativeQuery = true)
	String getEmail(String phone);
	
	@Query(value = "SELECT password FROM user WHERE phonenumber=?1",nativeQuery = true)
	String getHash(String phone);
	
	@Query(value = "SELECT username FROM user WHERE phonenumber=?1",nativeQuery = true)
	String getUsername(String phone);
	
	@Query(value = "SELECT account_deleted FROM user WHERE phonenumber=?1",nativeQuery = true)
	boolean getIsDeleted(String phone);

	@Query(value = "SELECT enabled FROM user WHERE phonenumber=?1",nativeQuery = true)
	boolean getIsActive(String phone);
	
	@Query(value = "SELECT account_deleted FROM user WHERE username=?1",nativeQuery = true)
	boolean getDeletedByUsername(String username);
	
	@Query(value = "SELECT * FROM user WHERE phonenumber=?1", nativeQuery = true)
	String getByPhone(String phoneNumber);

	@Query(value = "SELECT name,username,email FROM user WHERE phonenumber=?1", nativeQuery = true)
	String getForLogin(String phoneNumber);
	
	@Query(value = "SELECT * FROM user WHERE email=?1", nativeQuery = true)
	String getByEmail(String email);

	@Query(value = "SELECT user_id FROM user WHERE phonenumber=?1", nativeQuery = true)
	Long getUserId(String phoneNumber);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE user set last_login_ip=?1 WHERE phonenumber=?2", nativeQuery = true)
	void setLastIp(String ip,String phone);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE user set name=?1,updated_on=?2 WHERE phonenumber=?3", nativeQuery = true)
	void updateNameOnly(String name,Date updatedOn,String phone);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE user set name=?1,updated_on=?2 ,username=?3 WHERE phonenumber=?4", nativeQuery = true)
	void updateNameAndUser(String name,Date updatedOn,String username,String phone);
	
	@Query(value = "select * from user where phonenumber=?1", nativeQuery = true)
	User findByUserName(@Param("username") String phonenumber);
	
	@Query(value = "SELECT * FROM user WHERE username=?1", nativeQuery = true)
	String ifUserNameAlreadyPresent(String username);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE user set password=?1 WHERE phonenumber=?2", nativeQuery = true)
	void updatePassword(String password,String phone);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE user set account_deleted=true,enabled=false,account_deleted_on=?1 WHERE phonenumber=?2", nativeQuery = true)
	void deleteAccount(Date date,String phone);

	@Modifying
	@Transactional
	@Query(value = "UPDATE user set account_deleted=false,enabled=false WHERE phonenumber=?1", nativeQuery = true)
	void addAccountToDeleteRequest(String phone);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE user set account_deleted=false,enabled=true WHERE phonenumber=?1", nativeQuery = true)
	void recoverAccount(String phone);
	
}
