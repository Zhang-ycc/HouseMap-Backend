
package com.example.house_backend.service;

import com.alibaba.fastjson.JSONObject;
import com.example.house_backend.entity.House;
import com.example.house_backend.utils.SpatialRelationUtil;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface HouseService {
    List<House> getAllHouse();
    List<House> getFavors(Long userId);
    List<House> getScopeHouses(List<SpatialRelationUtil.Point> mPoints);
    List<House> getHousesInfo(Long id, int room, int min_price, int max_price);
    House findHouse_id(Long id);
    List<Map<String,Object>> getHouseAmountByDistrict(List<Map<String, Object>> points);
    List<Map<String, Object>> getHouseAmountByStreet(List<Map<String, Object>> points);

    List<Map<String, Object>> getHouseAmountByNeighbourhood(List<Map<String, Object>> points);

    List<JSONObject> getScopeHouseAmountByStreet(List<SpatialRelationUtil.Point> mPoints);

    List<JSONObject> getScopeHouseAmountByDistrict(List<SpatialRelationUtil.Point> mPoints);

    List<Map<String,Object>> getRecommendedHouses();
    List<Map<String,Object>> getUserRecommended(double userAvg);
    List<Map<String,Object>> getHouseRecommended(double houseAvg);
    List<JSONObject> getScopeHouseAmountByNeighbourhood(List<SpatialRelationUtil.Point> mPoints, List<Map<String, Object>> points);
}
