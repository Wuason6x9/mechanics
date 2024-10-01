package dev.wuason.mechanics.inventory.pages;

public class PageUtils {
    public static int getMaxPage(int size, int itemsPerPage) {
        return (int) Math.ceil((double) size / itemsPerPage);
    }
}
