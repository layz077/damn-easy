package com.example.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AccountDeletionRequests;

@Repository
public interface AccountDeletionRequestsRepository extends JpaRepository<AccountDeletionRequests, String>{

//	@Modifying
//	@Transactional
//	@Query(value = "UPDATE deletion_requests set account_deleted=?1,enabled=?2 WHERE phonenumber=?2", nativeQuery = true)
//	void addAccount(boolean deleted,boolean enabled,String phone);
}
