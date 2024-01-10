package dev.wuason.mechanics.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class NumberUtils {

    public static List<Integer> getRangeClosed(int start, int end) {
        return IntStream.rangeClosed(start, end).boxed().toList();
    }
    public static List<Integer> getRange(int start, int end) {
        return IntStream.range(start, end).boxed().toList();
    }
    //
    public static boolean inRange(int number, int start, int end) {
        return number >= start && number <= end;
    }

    public static boolean inRange(int number, Collection<Integer> range) {
        return range.contains(number);
    }

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
