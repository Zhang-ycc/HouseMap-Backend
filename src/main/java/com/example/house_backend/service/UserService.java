package com.example.house_backend.service;

import com.example.house_backend.entity.House;
import com.example.house_backend.entity.User;
import java.util.List;

public interface UserService {
    User checkUser(String email, String password);
    void addFavorHouse(Long userId,Long houseId);
    void deleteFavorHouse(Long userId,Long houseId);
    Integer findHouse(Long userId,Long houseId);
    User editInfo(Long userId, String username, String address, String telephone);

    User AddUser(String username, String email, String password);
}
