package com.example.house_backend.utils;

public class NumberUtil {
    public static int genRandomNum(int length){
        int num = 1;
        double random = Math.random();//随机生成一个0～1之间的数，不包含1
        if(random < 0.1){
            random = random + 0.1;
        }
        for(int i = 0; i < length; i++){
            num = num * 10;
        }
        return (int)((random * num));
    }
}
