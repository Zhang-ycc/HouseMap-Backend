package com.example.house_backend.daoimpl;

import com.example.house_backend.dao.UserDao;
import com.example.house_backend.entity.House;
import com.example.house_backend.entity.User;
import com.example.house_backend.repository.HouseRepository;
import com.example.house_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class UserDaoimpl implements UserDao {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Override
    public User findUserById(Long id) {
        return userRepository.findByUserId(id);
    }

    @Override
    public List<User> findUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public void addFavorHouse(House house, User user) {
        List<House> favorList = user.getFavors();
        favorList.add(house);
        user.setFavors(favorList);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteFavorHouse(House house, User user) {
        List<House> favorList = user.getFavors();
        favorList.remove(house);
        user.setFavors(favorList);
        userRepository.saveAndFlush(user);
    }

    @Override
    public Integer findHouse(House house,User user){
        List<House> favorList = user.getFavors();
        for (House o :
                favorList) {
            if(Objects.equals(o.getHouseId(), house.getHouseId()))
                return 1;
        }
        return 0;
    }

    @Override
    public List<House> find_UserId(User user) {
        return user.getFavors();
    }

    public User editInfo(Long userId, String username, String address, String telephone) {
        User user = userRepository.findByUserId(userId);
        user.setUsername(username);
        user.setAddress(address);
        user.setTelephone(telephone);
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User addUser(String username, String email, String password) {
        User user = new User();
        if (Objects.nonNull(userRepository.findByEmail(email)))
            return user;
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return userRepository.saveAndFlush(user);
    }

}
