package com.gzhuoj.usr.utils;

import java.util.Random;

public class GenerateRandStrUtil {
    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static Random rand = new Random();
    public static String getRandStr(int len) {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < len; i++) {
            result.append(characters.charAt(rand.nextInt(characters.length())));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(getRandStr(5));
        System.out.println(getRandStr(6));
        System.out.println(getRandStr(7));
    }
}
