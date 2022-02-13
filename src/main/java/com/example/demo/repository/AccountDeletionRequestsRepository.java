package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AccountDeletionRequests;

@Repository
public interface AccountDeletionRequestsRepository extends JpaRepository<AccountDeletionRequests, String>{

	@Query(value = "SELECT mobile_number FROM deletion_requests where permanent_delete_date=?1",nativeQuery = true)
    List<String> getDetails(Date date);
	
	@Modifying
	@Transactional
	@Query(value="DELETE from deletion_requests WHERE permanent_delete_date=?1",nativeQuery = true)
	void deleteEntry(Date date);
	
}
