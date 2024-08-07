package dev.wuason.mechanics.utils;

import java.util.Random;

public class MathUtils {
    /**
     * Generates a random number based on the provided number string.
     *
     * @param numbers The string representing the numbers. It can be a single number or a range delimited by a hyphen.
     *                If the string does not contain a hyphen, it is treated as a single number.
     *                If the string is null, then the method returns -1.
     *                If the range specified in the string is invalid or missing, then the default range of 64 is used.
     * @return The generated random number. If the number string is a single number, it returns that number.
     *         If the number string is a range, it returns a random number between the minimum and maximum values of the range.
     */
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
    /**
     * Generates a random integer between the given minimum and maximum values (inclusive).
     *
     * @param min the minimum value of the range (inclusive)
     * @param max the maximum value of the range (inclusive)
     * @return a random integer between min and max
     */
    public static int randomNumber(int min, int max){
        return (int) (min + Math.round(Math.random() * (max - min)));
    }

    /**
     * Generates a random integer between the given minimum and maximum values, with a specified chance affecting the distribution.
     * The chance parameter skews the distribution towards the minimum or maximum value.
     *
     * @param min The minimum value of the range (inclusive).
     * @param max The maximum value of the range (inclusive).
     * @param chance A value between 0 and 100 that affects the distribution of the random number.
     *               Higher values make the distribution skew towards the maximum value.
     * @return A random integer between min and max, influenced by the chance parameter.
     */
    public static int randomNumber(int min, int max, int chance) {
        double chanceDecimal = chance / 100.0;
        double range = max - min;
        return (int) Math.round(min + range * (1 - Math.pow(Math.random(), chanceDecimal)));
    }

    /**
     * Generates a random double between the given minimum and maximum values (inclusive).
     *
     * @param min The minimum value of the range (inclusive).
     * @param max The maximum value of the range (inclusive).
     * @return A random double between min and max.
     */
    public static double randomDouble(double min, double max) {
        return min + Math.random() * (max - min);
    }

    /**
     * Generates a random double between the given minimum and maximum values, with a specified chance affecting the distribution.
     * Similar to {@link #randomNumber(int, int, int)}, but returns a double and uses the chance parameter to skew the distribution.
     *
     * @param min The minimum value of the range (inclusive).
     * @param max The maximum value of the range (inclusive).
     * @param chance A value between 0 and 100 that affects the distribution of the random number.
     *               Higher values make the distribution skew towards the maximum value.
     * @return A random double between min and max, influenced by the chance parameter.
     */
    public static double randomDouble(double min, double max, int chance) {
        double chanceDecimal = chance / 100.0;
        double range = max - min;
        return min + range * (1 - Math.pow(Math.random(), chanceDecimal));
    }

    /**
     * Generates a random boolean value based on the given probability.
     *
     * @param probability The probability of returning true, must be between 0.0 and 100.0 (inclusive).
     * @return true if the random value is less than the probability, false otherwise.
     * @throws IllegalArgumentException if the probability is out of range.
     */
    public static boolean chance(float probability) {
        if (probability < 0.0f || probability > 100.0f) {
            throw new IllegalArgumentException("ERROR CHANCE!!!!!!!!!!");
        }
        float randomValue = new Random().nextFloat() * 100.0f;
        return randomValue < probability;
    }
}
