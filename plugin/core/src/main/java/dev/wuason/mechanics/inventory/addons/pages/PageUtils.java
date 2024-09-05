package dev.wuason.mechanics.inventory.addons.pages;

public class PageUtils {
    public static int getMaxPage(int size, int itemsPerPage) {
        return (int) Math.ceil((double) size / itemsPerPage);
    }
}
