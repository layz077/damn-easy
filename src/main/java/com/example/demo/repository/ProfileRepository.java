package com.example.demo.repository;

import com.example.demo.entity.ProfilePhotos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository  extends JpaRepository<ProfilePhotos,Long> {
}
