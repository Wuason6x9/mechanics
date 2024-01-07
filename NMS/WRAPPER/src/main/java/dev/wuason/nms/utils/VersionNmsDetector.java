package dev.wuason.nms.utils;

import org.bukkit.Bukkit;

public class VersionNmsDetector {

    private static ServerVersion serverVersion = null;
    public enum ServerVersion {
        v1_18_R1,
        v1_18_R2,
        v1_19_R1,
        v1_19_R2,
        v1_19_R3,
        v1_20_R1,
        v1_20_R2,
        v1_20_R3,
        UNSUPPORTED
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
    public static String getNMSVersion(){
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String versionName = packageName.substring(packageName.lastIndexOf('.') + 1);
        return versionName;
    }
}