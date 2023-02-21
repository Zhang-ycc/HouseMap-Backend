package com.example.house_backend.dao;

import com.alibaba.fastjson.JSONObject;
import com.example.house_backend.entity.District;
import com.example.house_backend.entity.House;
import com.example.house_backend.entity.Neighbourhood;
import com.example.house_backend.entity.Street;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface HouseDao {
    List<House> getAllHouse();
    House getHouse(Long id);
    List<House> getHousesInfo(Long id, int room, int min_price, int max_price);
    List<Map<String,Object>> getAmountByDistrict(int room, int min_price, int max_price);
    List<Map<String, Object>> getAmountByStreet(Double ln_low, Double ln_high, Double lat_low, Double lat_high, int room, int min_price, int max_price);
    List<Map<String,Object>> getAmountByNeighbourhood(Double ln_low, Double ln_high, Double lat_low, Double lat_high, int room, int min_price, int max_price);

    List<District> getAllDistrict();
    List<Street> getAllStreet();
    List<Map<String,Object>> getRecommendedHouses();
    List<Map<String,Object>> getUserRecommended(double userAvg);
    List<Map<String,Object>> getHouseRecommended(double houseAvg);
    List<Neighbourhood> getScreenRangeNeighbourhood(Double ln_low, Double ln_high, Double lat_low, Double lat_high);
}
