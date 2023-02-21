package com.example.house_backend.repository;

import com.example.house_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long userId);
    List<User> findByEmailAndPassword(String email, String password);

    User findByEmail(String email);
}
