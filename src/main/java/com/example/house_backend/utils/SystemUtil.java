package com.example.house_backend.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SystemUtil {
    public static String genToken(String src){
        if(src == null){
            return null;
        }
        try{
            //MessageDigest是Java自带的加密类，XX.getInstance("MD5")意为加密MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的byte来更新md
            md.update(src.getBytes());
            //md.digest() 生成摘要,
            //new BigInteger(int sinnum,byte[] magnitude),构造方法，
            //-1表示负数，0表示0，1表示正数
            String result = new BigInteger(1,md.digest()).toString(16);
            if(result.length() == 31){
                result = result + "-";
            }
            System.out.println(result);
            return result;
        }catch(Exception e){
            return null;
        }
    }
}
