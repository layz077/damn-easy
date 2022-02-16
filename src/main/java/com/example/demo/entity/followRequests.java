package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "follow_requests")
public class followRequests {

    @Id
    @Column(name = "follow_request_sender")
    private String followRequestSender;
    @Column(name = "follow_request_receiver")
    private String followRequestReceiver;
    @Column(name = "follow_request_send_date")
    private Date followRequestSendDate;
}
