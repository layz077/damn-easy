package com.example.demo.repository;

import com.example.demo.entity.ProfilePhotosArchived;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;

@Repository
public interface ProfilePhotosArchivedRepository extends JpaRepository<ProfilePhotosArchived,String> {

    @Modifying
    @Transactional
    @Query(value = "INSERT into profile_photos_archived values(?1,?2,?3,?4)",nativeQuery = true)
    void insertData(String phoneNumber, String fileLocation, int count, Date uploadDate);
}
