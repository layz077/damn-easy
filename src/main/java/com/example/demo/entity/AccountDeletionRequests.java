package com.example.demo.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deletion_requests")
public class AccountDeletionRequests {

	@Id
	@Column(name = "mobile_number")
	private String mobileNumber;
	@Column(name ="request_date")
	private Date Date;
	@Column(name ="permanent_delete_Date")
	private Date permanentDeleteDate;
}
