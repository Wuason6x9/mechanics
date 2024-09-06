package dev.wuason.mechanics.utils;

import org.bukkit.Bukkit;

public class VersionDetector {

    private static ServerVersion serverVersion = null;

    // Enum para las versiones
    public enum ServerVersion {
        v1_18_2(2),
        v1_19(3),
        v1_19_1(4),
        v1_19_2(5),
        v1_19_3(6),
        v1_19_4(7),
        v1_20(8),
        v1_20_1(9),
        v1_20_2(10),
        v1_20_3(11),
        v1_20_4(12),
        v1_20_5(13),
        v1_20_6(14),
        v1_21(15),
        v1_21_1(16),
        UNSUPPORTED(-1);

        private final int versionNumber;

        ServerVersion(int versionNumber) {
            this.versionNumber = versionNumber;
        }

        public int getVersionNumber() {
            return versionNumber;
        }

        public String getVersionName() {
            return name().replace("v", "").replace("_", ".");
        }

        /**
         * Checks if the current server version is at least the specified version.
         *
         * @param otherVersion the version to compare with.
         * @return true if the current server version is at least the specified version, false otherwise.
         */
        public boolean isAtLeast(ServerVersion otherVersion) {
            return versionNumber >= otherVersion.versionNumber;
        }

        /**
         * Checks if the current ServerVersion is less than the specified ServerVersion.
         *
         * @param otherVersion the ServerVersion to compare with the current version
         * @return true if the current version is less than the specified version, otherwise false
         */
        public boolean isLessThan(ServerVersion otherVersion) {
            return versionNumber < otherVersion.versionNumber;
        }

        /**
         * Converts a version string to a ServerVersion enum constant.
         *
         * @param version The version string to convert.
         * @return The corresponding ServerVersion enum constant.
         */
        public static ServerVersion fromString(String version) {
            try {
                return valueOf("v" + version.replace(".", "_"));
            } catch (IllegalArgumentException e) {
                return UNSUPPORTED;
            }
        }

        public static ServerVersion fromVersionNumber(int i){
            for(ServerVersion srv : values()){
                if(srv.versionNumber == i) return srv;
            }

            return UNSUPPORTED;
        }
    }


    /**
     * Retrieves the version of the server.
     *
     * @return The server version.
     */
    public static ServerVersion getServerVersion() {
        if (serverVersion == null) {
            String versionName = Bukkit.getBukkitVersion().split("-")[0];
            serverVersion = ServerVersion.fromString(versionName);
        }
        return serverVersion;
    }
}
