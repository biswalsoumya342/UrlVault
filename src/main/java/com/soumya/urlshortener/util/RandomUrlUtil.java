package com.soumya.urlshortener.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Random;

public class RandomUrlUtil {

    public static String randomUrl(HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/";
        String CHAR_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Random random = new Random();

        StringBuilder str = new StringBuilder();
        for (int i = 0; i<8;i++){
            int index = random.nextInt(CHAR_POOL.length());
            str.append(CHAR_POOL.charAt(index));
        }
        return baseUrl+str.toString();
    }
}
