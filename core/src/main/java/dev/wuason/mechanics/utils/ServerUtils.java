package dev.wuason.mechanics.utils;

public class ServerUtils {
    public static boolean isPaperServer() {
        boolean isPaper = false;
        try {
            Class.forName("com.destroystokyo.paper.MaterialSetTag");
            isPaper = true;
        } catch (ClassNotFoundException ignored) {
        }
        return isPaper && !isFolia();
    }

    private static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
