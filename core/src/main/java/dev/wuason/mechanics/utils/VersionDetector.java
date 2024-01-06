package dev.wuason.mechanics.utils;

import org.bukkit.Bukkit;

public class VersionDetector {
    private static ServerVersion serverVersion = null;

    // Enum para las versiones
    public enum ServerVersion {
        v1_18(0),
        v1_18_1(1),
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
        UNSUPPORTED(-1);

        private final int versionNumber;

        ServerVersion(int versionNumber) {
            this.versionNumber = versionNumber;
        }

        public boolean isAtLeast(ServerVersion otherVersion) {
            return versionNumber >= otherVersion.versionNumber;
        }

        public boolean isLessThan(ServerVersion otherVersion) {
            return versionNumber < otherVersion.versionNumber;
        }

        public static ServerVersion fromString(String version) {
            try {
                return valueOf("v" + version.replace(".", "_"));
            } catch (IllegalArgumentException e) {
                return UNSUPPORTED;
            }
        }
    }

    // Método para obtener la versión del servidor
    public static ServerVersion getServerVersion() {
        if (serverVersion == null) {
            String versionName = Bukkit.getBukkitVersion().split("-")[0];
            serverVersion = ServerVersion.fromString(versionName);
        }
        return serverVersion;
    }
}
