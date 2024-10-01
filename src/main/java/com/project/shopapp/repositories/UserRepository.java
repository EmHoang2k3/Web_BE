package com.project.shopapp.repositories;

import com.project.shopapp.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserModel,Long> {
    Page<UserModel> findAll(Pageable pageable);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<UserModel> findByPhoneNumber(String phoneNumber);
}
