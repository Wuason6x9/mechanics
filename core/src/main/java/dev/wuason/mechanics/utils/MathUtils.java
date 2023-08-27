package dev.wuason.mechanics.utils;

import java.util.Random;

public class MathUtils {
    public static int randomNumberString(String numbers){
        if(numbers == null) return -1;
        if(!numbers.contains("-")) return Integer.parseInt(numbers);
        String[] nString = numbers.split("-");
        if(nString.length < 2) return Integer.parseInt(nString[0]);
        if(nString[1] == null || nString[0] == null) return 64;
        int min = Integer.parseInt(nString[0]);
        int max = Integer.parseInt(nString[1]);
        return randomNumber(min,max);
    }
    public static int randomNumber(int min, int max){
        return (int) (min + Math.round(Math.random() * (max - min)));
    }
    public static boolean chance(float probability) {
        if (probability < 0.0f || probability > 100.0f) {
            throw new IllegalArgumentException("ERROR CHANCE!!!!!!!!!!");
        }
        float randomValue = new Random().nextFloat() * 100.0f;
        return randomValue < probability;
    }
}
