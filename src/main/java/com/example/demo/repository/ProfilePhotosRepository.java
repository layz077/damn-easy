package com.example.demo.repository;

import com.example.demo.entity.ProfilePhotos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilePhotosRepository  extends JpaRepository<ProfilePhotos,String> {

    @Query(value = "SELECT * FROM profile_photos WHERE phonenumber=?1",nativeQuery = true)
    ProfilePhotos details(String phoneNumber);

    @Query(value = "SELECT update_count FROM profile_photos WHERE phonenumber=?1",nativeQuery = true)
    Integer countValue(String phoneNumber);
}
