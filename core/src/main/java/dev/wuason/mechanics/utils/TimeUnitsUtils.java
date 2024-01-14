package dev.wuason.mechanics.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUnitsUtils {

    private static Pattern pattern = Pattern.compile("(\\d+)([a-zA-Z]+)");

    public static Long parseTime(String time) {
        if (time == null) return 20L;

        try {
            return Long.parseLong(time) * 20L;
        }
        catch (NumberFormatException e) {
            Matcher matcher = pattern.matcher(time);
            if (matcher.find()) {
                int amount = Integer.parseInt(matcher.group(1));
                TimeUnit unit = TimeUnit.getUnit(matcher.group(2));
                if (unit != null) {
                    return (long) (amount * unit.getMultiplier());
                }
                else {
                    return amount * 20L;
                }
            }
        }

        return 20L;
    }


    public enum TimeUnit {
        TICK("t", 1),
        SECOND("s", 20),
        MINUTE("m", 1200),
        HOUR("h", 72000),
        DAY("d", 1728000);

        private final String unit;
        private final int multiplier;

        TimeUnit(String unit, int multiplier) {
            this.unit = unit;
            this.multiplier = multiplier;
        }

        public String getUnit() {
            return unit;
        }

        public int getMultiplier() {
            return multiplier;
        }

        public static TimeUnit getUnit(String unit) {
            for (TimeUnit timeUnit : TimeUnit.values()) {
                if (timeUnit.getUnit().equalsIgnoreCase(unit)) {
                    return timeUnit;
                }
            }
            return null;
        }
    }

}
