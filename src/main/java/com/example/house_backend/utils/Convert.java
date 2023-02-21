package com.example.house_backend.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.house_backend.entity.House;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Convert {

     public static List<JSONObject> houseInfo(List<House> houses){
         List<JSONObject> ans = new ArrayList<>();
         for (House house : houses){
             JSONObject object = new JSONObject();

             JSONObject properties = new JSONObject();
             properties.put("houseId", house.getHouseId());
             properties.put("key", "Marker"+house.getHouseId());
             properties.put("name", house.getName().replace("\n",""));
             properties.put("price", house.getPrice());

             JSONObject position = new JSONObject();
             position.put("latitude", house.getNeighbourhood().getLatitude());
             position.put("longitude", house.getNeighbourhood().getLongitude());

             object.put("position", position);
             object.put("properties", properties);

             ans.add(object);
         }
         return ans;
    }

    public static List<SpatialRelationUtil.Point> scopePoints(List<Map<String, Object>> points) {
        List<SpatialRelationUtil.Point> mPoints = new ArrayList<>();
        for (Map<String, Object> point : points) {
            BigDecimal latitude = BigDecimal.valueOf((Double) point.get("latitude"));
            latitude = latitude.setScale(6, RoundingMode.HALF_UP);
            BigDecimal longitude = BigDecimal.valueOf((Double) point.get("longitude"));
            longitude = longitude.setScale(6, RoundingMode.HALF_UP);
            mPoints.add(new SpatialRelationUtil.Point(latitude, longitude));
        }
        return mPoints;
    }
}
