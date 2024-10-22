package dev.wuason.mechanics.utils;

public class ServerUtils {

    /**
     * Checks if the server is running on a Paper server.
     *
     * This method attempts to load the Paper server class to determine if the current
     * server is based on Paper. If the class is successfully loaded, it indicates that
     * the server is a Paper server. Additionally, it performs a secondary check to
     * ensure the server is not running on Folia.
     *
     * @return true if the server is a Paper server, false otherwise
     */
    public static boolean isPaperServer() {
        boolean isPaper = false;
        try {
            Class.forName("com.destroystokyo.paper.MaterialSetTag");
            isPaper = true;
        } catch (ClassNotFoundException ignored) {
        }
        return isPaper && !isFolia();
    }

    /**
     * Checks if the current server implementation is Folia.
     * This method attempts to load the class associated with Folia,
     * and if successful, returns true, indicating that the server is Folia.
     *
     * @return true if the server is Folia, false otherwise.
     */
    private static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


}
