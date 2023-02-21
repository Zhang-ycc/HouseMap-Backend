package com.example.house_backend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.house_backend.entity.House;
import com.example.house_backend.repository.UserRepository;
import com.example.house_backend.service.HouseService;
import com.example.house_backend.utils.Convert;
import com.example.house_backend.utils.SpatialRelationUtil;
import com.sun.xml.bind.v2.util.QNameMap;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.example.house_backend.controller.UserController.tokens;

@RestController
public class HouseController {

    @Autowired
    private HouseService houseService;

    //请求所有房源信息
    @RequestMapping("/getAllHouses")
    public List<JSONObject> getAllHouses() {
        return Convert.houseInfo(houseService.getAllHouse());
    }

    //请求某一用户收藏的所有房源信息
    @RequestMapping("/getFavorHouses")
    public List<JSONObject> getFavorHouses(@RequestBody Map<String, Object> o) {
        String token = (String) o.get("token");
        if(tokens.contains(token))
        {
        Long userId=Long.valueOf((Integer) o.get("id"));
        List<House> houses=houseService.getFavors(userId);
        List<JSONObject> favors= new ArrayList<>();;
        for (House h : houses) {
            JSONObject object = new JSONObject();
            JSONObject properties = new JSONObject();
            properties.put("houseId", h.getHouseId());
            properties.put("name", h.getName().replace("\n",""));
            properties.put("price", h.getPrice());
            properties.put("introduction", h.getIntroduction());
            properties.put("bedroom", h.getBedroom());
            properties.put("livingroom", h.getLivingroom());
            properties.put("bathroom", h.getBathroom());
            properties.put("floor", h.getFloor());
            properties.put("direction", h.getFace());
            properties.put("area", h.getSquare());
            String images=h.getImages();
            String image=images.split("#", 1)[0];
            properties.put("image", image);

            String name = h.getName().replace("\n","");
            String[] m = name.split("·");
            String[] parts = m[1].split("");
            String title = "";
            Integer i = 0;
            for (; i < parts.length; i++) {
                if (Objects.equals(parts[i], "室")) {
                    break;
                }
            }
            for (Integer j = 0; j < i - 1; j++) {
                title += parts[j];
            }
            properties.put("type", m[0]);
            properties.put("title", title);

            object.put("properties", properties);
            favors.add(object);
        }
        return favors;
        }
        else{
            return null;
        }
    }

    //请求某一房源详情
    @RequestMapping("/getHouse")
    public JSONObject getHouse(@RequestParam("id") Long id){
        House house= houseService.findHouse_id(id);
//        System.out.println(id);
        JSONObject object = new JSONObject();

        JSONObject properties = new JSONObject();
        properties.put("houseId", house.getHouseId());
        properties.put("name", house.getName().replace("\n",""));
        properties.put("price", house.getPrice());
        properties.put("introduction", house.getIntroduction());
        properties.put("bedroom", house.getBedroom());
        properties.put("livingroom", house.getLivingroom());
        properties.put("bathroom", house.getBathroom());
        properties.put("floor", house.getFloor());
        properties.put("direction", house.getFace());
        properties.put("area", house.getSquare());

        String name = house.getName().replace("\n","");
        String[] m = name.split("·");
        String[] parts = m[1].split("");
        String title = "";
        Integer i = 0;
        for (; i < parts.length; i++) {
            if (Objects.equals(parts[i], "室")) {
                break;
            }
        }
        for (Integer j = 0; j < i - 1; j++) {
            title += parts[j];
        }

        properties.put("type", m[0]);
        properties.put("title", title);

        JSONObject position = new JSONObject();
        position.put("latitude", house.getNeighbourhood().getLatitude());
        position.put("longitude", house.getNeighbourhood().getLongitude());

        List<String> images = Arrays.asList(house.getImages().split("#"));
        properties.put("images",images);
        properties.put("detailUrl", house.getDetailURL());

        object.put("position", position);
        object.put("properties", properties);

        return object;
    }

    //请求某一范围内的房源信息
    @RequestMapping("/getScopeHouses")
    public List<JSONObject> getScopeHouses(@RequestBody List<Map<String, Object>> points) {
        List<SpatialRelationUtil.Point> mPoints = new ArrayList<>();
        for (Map<String, Object> point : points) {
            BigDecimal latitude = BigDecimal.valueOf((Double) point.get("latitude"));
            latitude = latitude.setScale(6, RoundingMode.HALF_UP);
            BigDecimal longitude = BigDecimal.valueOf((Double) point.get("longitude"));
            longitude = longitude.setScale(6, RoundingMode.HALF_UP);
            mPoints.add(new SpatialRelationUtil.Point(latitude, longitude));
        }
        return Convert.houseInfo(houseService.getScopeHouses(mPoints));
    }

    //按区请求房源数量
    @RequestMapping("/getHousesByDistrict")
    public String getHousesByDistrict(@RequestBody List<Map<String, Object>> Points) {
//        System.out.println("getHousesByDistrict");
        return JSON.toJSONString(houseService.getHouseAmountByDistrict(Points));
    }

    //按街道请求房源数量
    @RequestMapping("/getHousesByStreet")
    public String getHousesByStreet(@RequestBody List<Map<String, Object>> Points) {
//        System.out.println("getHousesByStreet");
        return JSON.toJSONString(houseService.getHouseAmountByStreet(Points));
    }

    //按小区请求房源数量
    @RequestMapping("/getHousesByNeighbourhood")
    public String getHousesByNeighbourhood(@RequestBody List<Map<String, Object>> Points) {
//        System.out.println(Points);
//        System.out.println("getHousesByNeighbourhood");
        return JSON.toJSONString(houseService.getHouseAmountByNeighbourhood(Points));
    }

    //按区请求区域筛选房源数量
    @RequestMapping("/getScopeHousesByDistrict")
    public List<JSONObject> getScopeHousesByDistrict(@RequestBody List<Map<String, Object>> points) {
//        System.out.println(JSON.toJSONString(houseService.getHouseAmountByDistrict()));
        return houseService.getScopeHouseAmountByDistrict(Convert.scopePoints(points));
    }

    //按街道请求区域筛选房源数量
    @RequestMapping("/getScopeHousesByStreet")
    public String getScopeHousesByStreet(@RequestBody List<Map<String, Object>> points) {
        return JSON.toJSONString(houseService.getScopeHouseAmountByStreet(Convert.scopePoints(points)));
    }

    //按小区请求区域筛选房源数量
    @RequestMapping("/getScopeHousesByNeighbourhood")
    public String getScopeHousesByNeighbourhood(@RequestBody JSONObject o) {
        List<Map<String, Object>> screenPoints = (List<Map<String, Object>>) o.get("screenPoints");
        List<Map<String, Object>> scopePoints = (List<Map<String, Object>>) o.get("scopePoints");

//        System.out.println(Points);
        return JSON.toJSONString(houseService.getScopeHouseAmountByNeighbourhood(Convert.scopePoints(scopePoints), screenPoints));
    }

    //请求某一小区的房源
    @RequestMapping("/getHouseInfo")
    public List<JSONObject> getHouseInfo(@RequestBody Map<String, Object> o) {
//        System.out.println(o.get("id"));
        Long neighbourhoodId=Long.valueOf((Integer) o.get("id"));
        int room = Integer.parseInt(o.get("room").toString());
        int min_price = Integer.parseInt(o.get("min").toString());
        int max_price = Integer.parseInt(o.get("max").toString());
        List<House> houses=houseService.getHousesInfo(neighbourhoodId, room, min_price, max_price);
        List<JSONObject> info= new ArrayList<>();;
        for (House h : houses) {
//            System.out.println(h.getHouseId());
            JSONObject object = new JSONObject();
            JSONObject properties = new JSONObject();
            properties.put("houseId", h.getHouseId());
            properties.put("name", h.getName().replace("\n",""));
            properties.put("price", h.getPrice());
            String images=h.getImages();
            String image=images.split("#", 1)[0];
            properties.put("image", image);

            String name = h.getName().replace("\n","");
            String[] m = name.split("·");
            String[] parts = m[1].split("");
            String title = "";
            Integer i = 0;
            for (; i < parts.length; i++) {
                if (Objects.equals(parts[i], "室")) {
                    break;
                }
            }
            for (Integer j = 0; j < i - 1; j++) {
                title += parts[j];
            }
            properties.put("type", m[0]);
            properties.put("title", title);
            properties.put("bedroom", h.getBedroom());
            properties.put("livingroom", h.getLivingroom());
            properties.put("bathroom", h.getBathroom());
            properties.put("floor", h.getFloor());
            properties.put("direction", h.getFace());
            properties.put("area", h.getSquare());

            object.put("properties", properties);

            double score=  (0.3294*h.getCollectors().size()/10+0.3875*(6000/(double)h.getPrice())+0.2831*h.getNeighbourhood().getScore());
//            System.out.println(h.getNeighbourhood().getScore());
//            System.out.println(score);
            object.put("score",score);
            info.add(object);
        }
        return info;
    }

    //推荐房源
    @RequestMapping("/getRecommendedHouses")
    public String getRecommendedHouses(@RequestBody Map<String, Object> o) {
        List<Map<String,Object>> houseIdList=houseService.getRecommendedHouses();
        List<Map<String,Object>> newHouse=new ArrayList<>();
        for (Map<String, Object> stringObjectMap : houseIdList) {
            Map<String, Object> newMap = new HashMap<>();
            String name = (String) stringObjectMap.get("name");
            name=name.replace("\n","");
            String[] m = name.split("·");
            String[] parts = m[1].split("");
            Object title = "";
            Integer k = 0;
            for (; k < parts.length; k++) {
                if (Objects.equals(parts[k], "室")) {
                    break;
                }
            }
            for (Integer j = 0; j < k - 1; j++) {
                title += parts[j];
            }
            Object type = m[0];
            newMap.put("title", title);
            newMap.put("type", type);

            String images =(String)stringObjectMap.get("images");
            String image=images.split("#", 1)[0];
            newMap.put("houseId",stringObjectMap.get("house_id"));
            newMap.put("image", image);
            newMap.put("price", stringObjectMap.get("price"));
            newMap.put("bedroom", stringObjectMap.get("bedroom"));
            newMap.put("livingroom", stringObjectMap.get("livingroom"));
            newMap.put("direction", stringObjectMap.get("face"));
            newMap.put("area", stringObjectMap.get("square"));
            newHouse.add(newMap);
        }
        System.out.println(JSON.toJSONString(newHouse));
        return JSON.toJSONString(newHouse);
    }

    //推荐房源，用户推荐
    @RequestMapping("/getUserRecommendedHouses")
    public String getUserRecommended(@RequestBody Map<String, Object> o) {
        Long userId=Long.valueOf((Integer) o.get("id"));
        List<House> houses=houseService.getFavors(userId);
        if(houses.isEmpty()) return JSON.toJSONString("null");
        double userAvg=0;
        for(House h:houses){
            //System.out.println(h.getCollectors().size());
            //System.out.println(h.getPrice());
            //System.out.println(h.getNeighbourhood().getScore());
            userAvg+=0.3294*h.getCollectors().size()/10+0.3875*(6000/h.getPrice())+0.2831*h.getNeighbourhood().getScore();
        }
        userAvg=userAvg/houses.size();
        System.out.println(userAvg);
        List<Map<String,Object>> usersList=houseService.getUserRecommended(userAvg);
        System.out.println(usersList);
        List<Map<String,Object>> userRecommendHouses=new ArrayList<>();
        for (int p=0;p<usersList.size();p++) {
            System.out.println(usersList.get(p).get("uid"));
            Long user_id = ((Integer) usersList.get(p).get("uid")).longValue();
            List<House> newhouses=houseService.getFavors(user_id);
            System.out.println(newhouses);
            for(House l:newhouses){
                Map<String, Object> newMap = new HashMap<>();
                String name = l.getName().replace("\n","");
                String[] m = name.split("·");
                String[] parts = m[1].split("");
                String title = "";
                Integer i = 0;
                for (; i < parts.length; i++) {
                    if (Objects.equals(parts[i], "室")) {
                        break;
                    }
                }
                for (Integer j = 0; j < i - 1; j++) {
                    title += parts[j];
                }
                newMap.put("type", m[0]);
                newMap.put("title", title);

                List<String> images = Arrays.asList(l.getImages().split("#"));
                String image= images.get(0);
                newMap.put("houseId",l.getHouseId());
                newMap.put("image", image);
                newMap.put("price", l.getPrice());
                newMap.put("bedroom", l.getBedroom());
                newMap.put("livingroom", l.getLivingroom());
                newMap.put("direction", l.getFace());
                newMap.put("area",l.getSquare());
                if(!userRecommendHouses.contains(newMap)){
                    userRecommendHouses.add(newMap);
                }
            }
        }
        System.out.println(JSON.toJSONString(userRecommendHouses));
        return JSON.toJSONString(userRecommendHouses);
    }

    //推荐房源,相似推荐
    @RequestMapping("/getHouseRecommended")
    public String getHouseRecommended(@RequestBody Map<String, Object> o) {
        //System.out.println("hello");
        Long userId=Long.valueOf((Integer) o.get("id"));
        List<House> houses=houseService.getFavors(userId);
        List<Map<String,Object>> newHouse=new ArrayList<>();
        if(houses.isEmpty()) return JSON.toJSONString("null");
        double houseAvg = 0;
        for(House h:houses){
            //System.out.println(h.getCollectors().size());
            //System.out.println(h.getPrice());
            //System.out.println(h.getNeighbourhood().getScore());
            houseAvg+=0.3294*h.getCollectors().size()/10+0.3875*(6000/(double)h.getPrice())+0.2831*h.getNeighbourhood().getScore();
        }
        houseAvg=houseAvg/houses.size();
        System.out.println(houseAvg);
//        System.out.println(houseAvg);
        List<Map<String,Object>> houseIdList=houseService.getHouseRecommended(houseAvg);


        System.out.println(JSON.toJSONString(houseIdList));
        for (Map<String, Object> stringObjectMap : houseIdList) {
            Map<String, Object> newMap = new HashMap<>();
            String name = (String) stringObjectMap.get("name");
            name=name.replace("\n","");
            String[] m = name.split("·");
            String[] parts = m[1].split("");
            Object title = "";
            Integer k = 0;
            for (; k < parts.length; k++) {
                if (Objects.equals(parts[k], "室")) {
                    break;
                }
            }
            for (Integer j = 0; j < k - 1; j++) {
                title += parts[j];
            }
            Object type = m[0];
            newMap.put("title", title);
            newMap.put("type", type);

            String images =(String)stringObjectMap.get("images");
            String[] Img=images.split("#");
            String image= Img[0];
            //System.out.println(image);
            newMap.put("houseId",stringObjectMap.get("house_id"));
            newMap.put("image", image);
            newMap.put("price", stringObjectMap.get("price"));
            newMap.put("bedroom", stringObjectMap.get("bedroom"));
            newMap.put("livingroom", stringObjectMap.get("livingroom"));
            newMap.put("direction", stringObjectMap.get("face"));
            newMap.put("area", stringObjectMap.get("square"));
            newHouse.add(newMap);
        }
//        System.out.println(JSON.toJSONString(newHouse));
        return JSON.toJSONString(newHouse);
    }
}
