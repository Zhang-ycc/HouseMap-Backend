package com.example.house_backend.serviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.example.house_backend.dao.HouseDao;
import com.example.house_backend.dao.UserDao;
import com.example.house_backend.entity.*;
import com.example.house_backend.service.HouseService;
import com.example.house_backend.utils.SpatialRelationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Double.*;

@Service
public class HouseServiceimpl implements HouseService {

    @Autowired
    private HouseDao houseDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<House> getAllHouse() {
        return houseDao.getAllHouse();
    }

    @Override
    public List<House> getFavors(Long userId) {
        User user = userDao.findUserById(userId);
        return userDao.find_UserId(user);
    }

    @Override
    public House findHouse_id(Long id){
        return houseDao.getHouse(id);
    }

    @Override
    public List<House> getHousesInfo(Long id, int room, int min_price, int max_price){
        return houseDao.getHousesInfo(id, room, min_price, max_price);
    }

    @Override
    public List<Map<String,Object>> getHouseAmountByDistrict(List<Map<String, Object>> points)
    {
        int room = Integer.parseInt(points.get(0).get("room").toString());
        int min_price = Integer.parseInt(points.get(0).get("min").toString());
        int max_price = Integer.parseInt(points.get(0).get("max").toString());
        return houseDao.getAmountByDistrict(room, min_price, max_price);
    }

    @Override
    public List<Map<String, Object>> getHouseAmountByStreet(List<Map<String, Object>> points) {
        double a = parseDouble(points.get(0).get("longitude").toString());
        double b = parseDouble(points.get(1).get("longitude").toString());
        double c = parseDouble(points.get(0).get("latitude").toString());
        double d = parseDouble(points.get(1).get("latitude").toString());
        int room = Integer.parseInt(points.get(2).get("room").toString());
        int min_price = Integer.parseInt(points.get(2).get("min").toString());
        int max_price = Integer.parseInt(points.get(2).get("max").toString());
        return houseDao.getAmountByStreet(min(a,b),max(a,b),min(c,d),max(c,d), room, min_price, max_price);
    }

    @Override
    public List<Map<String,Object>> getHouseAmountByNeighbourhood(List<Map<String, Object>> points) {
        double a = parseDouble(points.get(0).get("longitude").toString());
        double b = parseDouble(points.get(1).get("longitude").toString());
        double c = parseDouble(points.get(0).get("latitude").toString());
        double d = parseDouble(points.get(1).get("latitude").toString());
        int room = Integer.parseInt(points.get(2).get("room").toString());
        int min_price = Integer.parseInt(points.get(2).get("min").toString());
        int max_price = Integer.parseInt(points.get(2).get("max").toString());
        return houseDao.getAmountByNeighbourhood(min(a,b),max(a,b),min(c,d),max(c,d), room, min_price, max_price);
    }

    @Override
    public List<House> getScopeHouses(List<SpatialRelationUtil.Point> mPoints) {
        List<House> houses = houseDao.getAllHouse();
        List<House> ret = new ArrayList<>();
        for (House house : houses) {
            SpatialRelationUtil.Point point = new SpatialRelationUtil.Point(house.getNeighbourhood().getLatitude(), house.getNeighbourhood().getLongitude());
            if (SpatialRelationUtil.isPolygonContainsPoint(mPoints, point)) {
                ret.add(house);
            }
        }
        return ret;
    }


    @Override
    public List<JSONObject> getScopeHouseAmountByDistrict(List<SpatialRelationUtil.Point> mPoints) {
        List<JSONObject> ret = new ArrayList<>();
        List<District> districts = houseDao.getAllDistrict();
        for (District district : districts) {
            Long amount = 0L;
            for (House house : district.getHouses()) {
                SpatialRelationUtil.Point point = new SpatialRelationUtil.Point(house.getNeighbourhood().getLatitude(), house.getNeighbourhood().getLongitude());
                if (SpatialRelationUtil.isPolygonContainsPoint(mPoints, point)) {
                    ++ amount;
                }
            }
            JSONObject object = new JSONObject();
            object.put("name", district.getName());
            object.put("amount", amount);
            object.put("latitude", district.getLatitude());
            object.put("longitude", district.getLongitude());
            ret.add(object);
        }
        return ret;
    }
    @Override
    public List<JSONObject> getScopeHouseAmountByStreet(List<SpatialRelationUtil.Point> mPoints) {
        List<JSONObject> ret = new ArrayList<>();
        List<Street> streets = houseDao.getAllStreet();
        for (Street street : streets) {
            Long amount = 0L;
            for (House house : street.getHouses()) {
                SpatialRelationUtil.Point point = new SpatialRelationUtil.Point(house.getNeighbourhood().getLatitude(), house.getNeighbourhood().getLongitude());
                if (SpatialRelationUtil.isPolygonContainsPoint(mPoints, point)) {
                    ++ amount;
                }
            }
            JSONObject object = new JSONObject();
            object.put("name", street.getName());
            object.put("amount", amount);
            object.put("latitude", street.getLatitude());
            object.put("longitude", street.getLongitude());
            ret.add(object);
        }
        return ret;
    }
    @Override
    public List<JSONObject> getScopeHouseAmountByNeighbourhood(List<SpatialRelationUtil.Point> mPoints, List<Map<String, Object>> points) {
        double a = parseDouble(points.get(0).get("longitude").toString());
        double b = parseDouble(points.get(1).get("longitude").toString());
        double c = parseDouble(points.get(0).get("latitude").toString());
        double d = parseDouble(points.get(1).get("latitude").toString());

        List<JSONObject> ret = new ArrayList<>();
        List<Neighbourhood> neighbourhoods = houseDao.getScreenRangeNeighbourhood(min(a,b),max(a,b),min(c,d),max(c,d));
        for (Neighbourhood neighbourhood : neighbourhoods) {
//            System.out.println(neighbourhood);
            Long amount = 0L;
            for (House house : neighbourhood.getHouses()) {
                SpatialRelationUtil.Point point = new SpatialRelationUtil.Point(house.getNeighbourhood().getLatitude(), house.getNeighbourhood().getLongitude());
                if (SpatialRelationUtil.isPolygonContainsPoint(mPoints, point)) {
                    ++ amount;
                }
            }
            JSONObject object = new JSONObject();
            object.put("name", neighbourhood.getName());
            object.put("amount", amount);
            object.put("latitude", neighbourhood.getLatitude());
            object.put("longitude", neighbourhood.getLongitude());
            ret.add(object);
        }
        return ret;
    }

    @Override
    public List<Map<String,Object>> getRecommendedHouses(){
        return houseDao.getRecommendedHouses();
    }

    @Override
    public List<Map<String,Object>> getUserRecommended(double userAvg){
        return houseDao.getUserRecommended(userAvg);
    }

    @Override
    public List<Map<String,Object>> getHouseRecommended(double houseAvg){
        return houseDao.getHouseRecommended(houseAvg);
    }
}
