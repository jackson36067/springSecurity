package com.jackson.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class JWTUtils {

    public static  String scret = "abcdefjhijklmnopqrstuvwxyz";

    public static Long time = 7 * 24 * 60 * 60 * 1000L;  //7天

    public  static String createTokenMap(Map<String,String> map) {

        JWTCreator.Builder builder = JWT.create();
        for (Map.Entry<String, String> entry : map.entrySet())     {
            builder.withClaim(entry.getKey(), entry.getValue());

        }
        builder.withExpiresAt(new Date(System.currentTimeMillis() + time));
        String token = builder.sign(Algorithm.HMAC256(scret));
        return token;
    }
    public static String createToken(String key , String value) {

        JWTCreator.Builder builder = JWT.create();
        builder.withClaim(key,value);
        String token = builder.sign(Algorithm.HMAC256(scret));
        return token;
    }

    public  static String getToken(String token,String key){
        //先验证签名
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(scret)).build();
        //验证其他信息
        DecodedJWT verify = verifier.verify(token);
        String value = verify.getClaim(key).asString();
        return value;
    }
    public static  boolean isExpired(String token){
        //先验证签名
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(scret)).build();

        try {
            //验证其他信息
            DecodedJWT verify = verifier.verify(token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}