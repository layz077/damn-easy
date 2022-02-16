package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile_photos")
public class ProfilePhotos {

    @Id
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "file_location")
    private String fileLocation;
}
