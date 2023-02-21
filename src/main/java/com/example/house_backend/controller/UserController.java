package com.example.house_backend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.house_backend.entity.User;
import com.example.house_backend.service.UserService;
import com.example.house_backend.utils.NumberUtil;
import com.example.house_backend.utils.SystemUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //检查用户名和密码是否正确，并返回用户信息

    public static List tokens=new ArrayList<>();

    @SneakyThrows
    @RequestMapping("/logIn")
    public JSONObject logIn(@RequestBody Map<String, Object> o) {
        String email = (String)o.get("email");
        String password = (String)o.get("password");
        User user=userService.checkUser(email, password);
        String src;
        if(user==null){
            JSONObject userinfo = new JSONObject();
            userinfo.put("user",user);
            return userinfo;
        }
        src = String.valueOf(user.getUserId() + NumberUtil.genRandomNum(4));
//        System.out.println(src);
//        System.out.println("1");
        String token=JSON.toJSONString(SystemUtil.genToken(src));
        tokens.add(token);
        JSONObject userinfo = new JSONObject();
        userinfo.put("user",user);
        userinfo.put("token",token);
        return userinfo;
    }

//    @RequestMapping("/getProfile")
//    public User getProfile(@RequestBody Map<String, Object> o) {
//        String token = JSON.toJSONString((String) o.get("token"));
////        System.out.println("2");
////        System.out.println(token);
////        System.out.println(tokens);
////        System.out.println(tokens.contains(token));
//        if(tokens.contains(token))
//        {
////            System.out.println("3");
////            System.out.println(token);
//            User user=userService.checkUser("2606310971@qq.com","314159262528hxy");
//            return user;
//        }
//        else
//            return null;
//    }

    //收藏页面增加房源
    @RequestMapping("/addHouse")
    public String addHouse(@RequestBody Map<String, String> o){
        Long houseId=Long.valueOf((String)o.get("houseId"));
        Long userId=Long.valueOf((String)o.get("userId"));
        userService.addFavorHouse(userId,houseId);
        return JSON.toJSONString(null);
    }

    //收藏页面删除房源
    @RequestMapping("/deleteHouse")
    public String deleteHouse(@RequestBody Map<String, String> o){
        Long houseId=Long.valueOf((String)o.get("houseId"));
        Long userId=Long.valueOf((String)o.get("userId"));
        userService.deleteFavorHouse(userId,houseId);
        return JSON.toJSONString(null);
    }

    //检查某一房源是否被某用户收藏
    @RequestMapping("/findHouse")
    public Integer findHouse(@RequestBody Map<String, Object> o){
        Long houseId=Long.valueOf((Integer)o.get("houseId"));
        Long userId=Long.valueOf((Integer)o.get("userId"));
        return userService.findHouse(userId,houseId);
    }

    @RequestMapping("/editInfo")
    public User editInfo(@RequestBody Map<String, Object> o) {
        Long userId = Long.valueOf((Integer) o.get("userId"));
        String username = (String)o.get("username");
        String address = (String)o.get("address");
        String telephone = (String)o.get("telephone");
        return userService.editInfo(userId, username, address, telephone);
    }

    @RequestMapping("/signUp")
    public User signUp(@RequestBody Map<String, Object> o) {
        String username = (String)o.get("username");
        String email = (String)o.get("email");
        String password = (String)o.get("password");
        return userService.AddUser(username, email, password);
    }
}
