package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	@Query(value = "SELECT * FROM user WHERE id=?1 AND password=?2",nativeQuery = true)
	String getUser(long id, String password);
	
	@Query(value = "SELECT password FROM user WHERE id=?1",nativeQuery = true)
	String getHash(long id);
	
	@Query(value = "SELECT * FROM user WHERE phone_number=?1", nativeQuery = true)
	String getByPhone(String phoneNumber);
}
