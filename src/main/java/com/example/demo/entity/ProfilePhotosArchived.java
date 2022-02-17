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
@Table(name = "profile_photos_archived")
public class ProfilePhotosArchived {

    @Id
    @Column(name = "phonenumber")
    private String phoneNumber;
    @Column(name = "file_location")
    private String fileLocation;
    @Column(name = "date_of_upload")
    private Date uploadDate;
    @Column(name = "update_count")
    private int updateCount;
}
