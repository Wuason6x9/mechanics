package dev.wuason.mechanics.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class AdvancedUtils {
    public static Unsafe getUnsafe() {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            return (Unsafe) unsafeField.get(null);
        } catch (Throwable t) {
            return null;
        }
    }

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
