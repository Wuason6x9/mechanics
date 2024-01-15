package dev.wuason.mechanics.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class NumberUtils {

    /**
     * Returns a list of integers that represent the range between a given start and end value, inclusive.
     *
     * @param start the starting value of the range (inclusive)
     * @param end   the ending value of the range (inclusive)
     * @return a list of integers representing the range between start and end, inclusive
     */
    public static List<Integer> getRangeClosed(int start, int end) {
        return IntStream.rangeClosed(start, end).boxed().toList();
    }
    /**
     * Returns a list of integers representing a range of values.
     *
     * @param start the starting value of the range (inclusive)
     * @param end   the ending value of the range (exclusive)
     * @return a list of integers representing the range
     */
    public static List<Integer> getRange(int start, int end) {
        return IntStream.range(start, end).boxed().toList();
    }

    /**
     * Determines whether the given number is within the specified range.
     *
     * @param number the number to check
     * @param start  the start of the range (inclusive)
     * @param end    the end of the range (inclusive)
     * @return true if the number is within the range, false otherwise
     */
    public static boolean inRange(int number, int start, int end) {
        return number >= start && number <= end;
    }

    /**
     * Checks if a given number is within a specified range.
     *
     * @param number the number to check
     * @param range  the collection representing the range
     * @return true if the number is within the range, false otherwise
     */
    public static boolean inRange(int number, Collection<Integer> range) {
        return range.contains(number);
    }

    /**
     * Retrieves a list of integers based on the provided range format.
     *
     * @param format the range format to retrieve the list from. The format should be in the form of "start-end".
     *               If the format does not contain a hyphen, it is considered a single number.
     *               Example formats: "1-5", "10-20", "7"
     * @return a list of integers based on the range format.
     *         If the format is a single number, the list will contain only that number.
     *         If the format is a range, the list will contain all numbers within the range, inclusive.
     * @throws NumberFormatException if the format cannot be parsed into valid integer values.
     * @throws UnsupportedOperationException if an invalid range format is provided.
     */
    public static List<Integer> getRangeFormat(String format){//1-2
        ArrayList<Integer> range = new ArrayList<>();
        if(!format.contains("-")){
            range.add(Integer.parseInt(format));
            return Collections.unmodifiableList(range);
        }
        String[] split = format.split("-");
        int start = Integer.parseInt(split[0]);
        int end = Integer.parseInt(split[1]);
        range.addAll(getRange(start,end));
        return Collections.unmodifiableList(range);
    }

    /**
     * Returns a list of integers representing a closed range based on the given format.
     * The format can be either a single integer or a range specified by two integers separated with a hyphen.
     *
     * @param format the format of the range (either a single integer or a range specified as "start-end")
     * @return a list of integers representing the closed range based on the given format
     */
    public static List<Integer> getRangeFormatClosed(String format){//1-2
        ArrayList<Integer> range = new ArrayList<>();
        if(!format.contains("-")){
            range.add(Integer.parseInt(format));
            return Collections.unmodifiableList(range);
        }
        String[] split = format.split("-");
        int start = Integer.parseInt(split[0]);
        int end = Integer.parseInt(split[1]);
        range.addAll(getRangeClosed(start,end));
        return Collections.unmodifiableList(range);
    }
}
