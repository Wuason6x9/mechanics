package dev.wuason.mechanics.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUnitsUtils {

    /**
     * Compiled regular expression pattern used for extracting numeric values followed by
     * alphabetic characters. The pattern captures groups to separate the numeric and alphabetic parts.
     */
    private static Pattern pattern = Pattern.compile("(\\d+)([a-zA-Z]+)");

    /**
     * Parses a given time string and converts it to an equivalent duration in ticks.
     * The input string can be a plain numeric value representing ticks or a compound
     * value consisting of a number followed by a time unit symbol (e.g., "5s" for 5 seconds).
     *
     * @param time the time string to parse. It can be null, a numeric value, or a string
     *             with a unit symbol (e.g., "5s" for 5 seconds).
     * @return the corresponding time in ticks. Returns 20 ticks if the input is null
     *         or cannot be parsed.
     */
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

    /**
     * Converts a given time and its associated time unit to milliseconds.
     *
     * @param time the amount of time to convert
     * @param unit the time unit of the input time
     * @return the equivalent time in milliseconds
     */
    public static long toMilliSeconds(long time, TimeUnit unit) {
        return (time * unit.getMultiplier()) * 50L;
    }


    /**
     * The enumeration TimeUnit is used to represent various units of time,
     * such as tick, second, minute, hour, and day.
     * Each unit has a symbol and a multiplier that defines its conversion rate
     * with respect to the base unit (TICK).
     */
    public enum TimeUnit {
        /**
         * Represents a unit of time equivalent to a tick.
         * A tick is the smallest unit of time measurement in this enumeration.
         *
         * Unit: "t"
         * Multiplier: 1
         */
        TICK("t", 1),
        /**
         * Represents the time unit "second" with a unit abbreviation "s" and a
         * multiplier of 20.
         */
        SECOND("s", 20),
        /**
         * Time unit representing a minute.
         * The shorthand unit is "m" and it corresponds to 1200 ticks.
         */
        MINUTE("m", 1200),
        /**
         * Represents an hour in the TimeUnit enum.
         * The abbreviation for this unit is "h" and it corresponds to 72000 ticks.
         */
        HOUR("h", 72000),
        /**
         * Represents a time unit of one day.
         * The unit abbreviation is "d".
         * The multiplier value is 1728000.
         */
        DAY("d", 1728000);

        /**
         * The text representation of the time unit.
         * Examples include "t" for TICK, "s" for SECOND, "m" for MINUTE, "h" for HOUR, and "d" for DAY.
         */
        private final String unit;
        /**
         * The multiplier that defines the conversion rate of this TimeUnit to the base unit (TICK).
         */
        private final int multiplier;

        /**
         * Constructs a TimeUnit with the specified unit symbol and multiplier.
         *
         * @param unit the symbol representing the time unit (e.g., "s" for seconds)
         * @param multiplier the value to multiply to get the time unit in base units
         */
        TimeUnit(String unit, int multiplier) {
            this.unit = unit;
            this.multiplier = multiplier;
        }

        /**
         * Returns the unit abbreviation for the time unit.
         *
         * @return the unit abbreviation as a String
         */
        public String getUnit() {
            return unit;
        }

        /**
         * Returns the multiplier associated with this time unit.
         *
         * @return the multiplier for this time unit
         */
        public int getMultiplier() {
            return multiplier;
        }

        /**
         * Retrieves the TimeUnit associated with a given string representation.
         *
         * @param unit the string representation of the time unit (case insensitive)
         * @return the corresponding TimeUnit, or null if no match is found
         */
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
