package com.example.house_backend.serviceimpl;

import com.example.house_backend.dao.UserDao;
import com.example.house_backend.dao.HouseDao;
import com.example.house_backend.entity.House;
import com.example.house_backend.entity.User;
import com.example.house_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private HouseDao houseDao;

    @Override
    public User checkUser(String email, String password) {
        List<User> list = userDao.findUserByEmailAndPassword(email, password);
        if (list.isEmpty()) return null;
        else {
            list.get(0).setPassword(null);
            return list.get(0);
        }
    }

    @Override
    public void addFavorHouse(Long userId, Long houseId) {
        User user=userDao.findUserById(userId);
        House house=houseDao.getHouse(houseId);
        userDao.addFavorHouse(house,user);
    }

    @Override
    public void deleteFavorHouse(Long userId, Long houseId) {
        User user=userDao.findUserById(userId);
        House house=houseDao.getHouse(houseId);
        userDao.deleteFavorHouse(house,user);
    }

    @Override
    public Integer findHouse(Long userId,Long houseId) {
        User user = userDao.findUserById(userId);
        House house = houseDao.getHouse(houseId);
        return userDao.findHouse(house, user);
    }

    public User editInfo(Long userId, String username, String address, String telephone) {
        return userDao.editInfo(userId, username, address, telephone);
    }

    @Override
    public User AddUser(String username, String email, String password) {
        return userDao.addUser(username, email, password);
    }
}
