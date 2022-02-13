package com.example.demo.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "follow_table")
public class FollowTable {
	
	@Id
	@Column(name="follow_request_sender")
	private String followRequestSender;
	@Column(name="follow_request_receiver")
	private String followRequestReceiver;

}
