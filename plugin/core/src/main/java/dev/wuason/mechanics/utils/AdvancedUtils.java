package dev.wuason.mechanics.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Arrays;

public class AdvancedUtils {
    /**
     * Retrieves an instance of the {@link Unsafe} class by accessing its private field.
     *
     * @return An instance of the {@link Unsafe} class, or null if the instance could not be retrieved.
     */
    public static Unsafe getUnsafe() {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            return (Unsafe) unsafeField.get(null);
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * Fetches the value of a specified field from an instance of a given class.
     *
     * @param clazz the class containing the field
     * @param instance the object instance from which the field value is to be fetched
     * @param fieldName the name of the field whose value is to be fetched
     * @return the value of the specified field, or null if an error occurs
     */
    public static Object fetchField(final Class<?> clazz, final Object instance, final String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            long offset = getUnsafe().objectFieldOffset(field);
            return getUnsafe().getObject(instance, offset);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    /**
     * Sets the value of the specified field in the given instance of the specified class.
     *
     * @param clazz The Class object representing the class of the field.
     * @param instance The object instance in which to set the field value.
     * @param fieldName The name of the field to be set.
     * @param value The value to set in the specified field.
     */
    public static void setField(final Class<?> clazz, final Object instance, final String fieldName, final Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            long offset = getUnsafe().objectFieldOffset(field);
            getUnsafe().putObject(instance, offset, value);
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

}
