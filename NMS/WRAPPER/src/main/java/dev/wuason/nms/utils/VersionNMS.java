package dev.wuason.nms.utils;

import org.bukkit.Bukkit;

public class VersionNMS {

    private static ServerVersion serverVersion = null;

    public enum ServerVersion {
        v1_18_R1(new int[]{0, 1}),
        v1_18_R2(new int[]{2}),
        v1_19_R1(new int[]{3, 4, 5}),
        v1_19_R2(new int[]{6}),
        v1_19_R3(new int[]{7}),
        v1_20_R1(new int[]{8, 9}),
        v1_20_R2(new int[]{10}),
        v1_20_R3(new int[]{11, 12}),
        UNSUPPORTED(new int[]{-1});

        private final int[] versions;

        ServerVersion(int[] versions) {
            this.versions = versions;
        }

        public int[] getVersions() {
            return versions;
        }
    }


    public static ServerVersion getServerVersion() {
        if (serverVersion == null) {
            String versionName = getNMSVersion();
            try {
                serverVersion = ServerVersion.valueOf(versionName);
            } catch (IllegalArgumentException e) {
                serverVersion = ServerVersion.UNSUPPORTED;
            }
        }
        return serverVersion;
    }

    public static String getNMSVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String versionName = packageName.substring(packageName.lastIndexOf('.') + 1);
        return versionName;
    }
}