package com.example.house_backend.daoimpl;

import com.alibaba.fastjson.JSONObject;
import com.example.house_backend.dao.HouseDao;
import com.example.house_backend.entity.*;
import com.example.house_backend.repository.*;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class HouseDaoimpl implements HouseDao {

    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private StreetRepository streetRepository;
    @Autowired
    private NeighbourhoodRepository neighbourhoodRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<House> getAllHouse() {
        return houseRepository.findAll();
    }

    @Override
    public House getHouse(Long id){
        return houseRepository.getOne(id);
    }

    @Override
    public List<House> getHousesInfo(Long id, int room, int min_price, int max_price){
        Neighbourhood neighbourhood=neighbourhoodRepository.getNeighbourhoodByNeighbourhoodId(id);
        //System.out.println(neighbourhood);
        List<House> houses = neighbourhood.getHouses();
        List<House> result = null;
        result = houses.stream()
                .filter((House h)->{
                    boolean flag = true;
                    if (room == 0 || room >= 4)
                        flag = h.getBedroom() >= room;
                    else
                        flag = h.getBedroom() == room;
                    if (max_price == 0 || max_price == 12000)
                        flag = flag && (h.getPrice() >= min_price);
                    else
                        flag = flag && (h.getPrice() >= min_price && h.getPrice() <= max_price);
                    return flag;
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<Map<String,Object>> getAmountByDistrict(int room, int min_price, int max_price) {
        if(room == 0 && min_price == 0 && max_price == 0)
            return houseRepository.getAmountByDistrict();
        else {
            int min_room, max_room;
            if (room == 0) {min_room = 0; max_room = 9999;}
            else if (room == 4) {min_room = 4; max_room = 9999;}
            else {min_room = max_room = room;}
            if (max_price == 0 || max_price == 12000) max_price = 999999999;
            return houseRepository.getFilteredAmountByDistrict(min_room, max_room, min_price, max_price);
        }

    }

    @Override
    public List<Map<String, Object>> getAmountByStreet(Double ln_low, Double ln_high, Double lat_low, Double lat_high, int room, int min_price, int max_price) {
        int min_room, max_room;
        if (room == 0) {min_room = 0; max_room = 9999;}
        else if (room == 4) {min_room = 4; max_room = 9999;}
        else {min_room = max_room = room;}
        if (max_price == 0 || max_price == 12000) max_price = 999999999;
        return houseRepository.getAmountByStreet(ln_low, ln_high, lat_low, lat_high, min_room, max_room, min_price, max_price);
    }

    @Override
    public List<Map<String,Object>> getAmountByNeighbourhood(Double ln_low, Double ln_high, Double lat_low, Double lat_high, int room, int min_price, int max_price) {
        int min_room, max_room;
        if (room == 0) {min_room = 0; max_room = 9999;}
        else if (room == 4) {min_room = 4; max_room = 9999;}
        else {min_room = max_room = room;}
        if (max_price == 0 || max_price == 12000) max_price = 999999999;
        return houseRepository.getAmountByNeighbourhood(ln_low, ln_high, lat_low, lat_high, min_room, max_room, min_price, max_price);
    }

    @Override
    public List<District> getAllDistrict() {
        return districtRepository.findAll();
    }

    @Override
    public List<Street> getAllStreet() {
        return streetRepository.findAll();
    }

    @Override
    public List<Map<String,Object>> getRecommendedHouses() {
        List<Map<String,Object>> houses = houseRepository.getRecommendedHouses();
        //System.out.println(houses);
        return houses;
    }

    @Override
    public List<Map<String,Object>> getUserRecommended(double userAvg){
        List<Map<String,Object>> users = houseRepository.getUserRecommended(userAvg);
        return users;
    }

    @Override
    public List<Map<String,Object>> getHouseRecommended(double houseAvg){
        List<Map<String,Object>> houses = houseRepository.getHouseRecommended(houseAvg);
        return houses;
    }

    @Override
    public List<Neighbourhood> getScreenRangeNeighbourhood(Double ln_low, Double ln_high, Double lat_low, Double lat_high) {
        return neighbourhoodRepository.getScreenRangeNeighbourhood(ln_low, ln_high, lat_low, lat_high);
    }
}
