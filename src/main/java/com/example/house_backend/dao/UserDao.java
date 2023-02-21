package com.example.house_backend.dao;

import com.example.house_backend.entity.House;
import com.example.house_backend.entity.User;

import java.util.List;

public interface UserDao {
    User findUserById(Long id);
    List<User> findUserByEmailAndPassword(String email, String password);
    void addFavorHouse(House house, User user);
    void deleteFavorHouse(House house,User user);
    Integer findHouse(House house,User user);
    List<House> find_UserId(User user);
    User editInfo(Long userId, String username, String address, String telephone);

    User addUser(String username, String email, String password);
}
