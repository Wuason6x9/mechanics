package dev.wuason.mechanics.utils.version;

import org.bukkit.Bukkit;

public enum MinecraftVersion {
    v1_18_2(2, NMSVersion.v1_18_R2),
    v1_19(3, NMSVersion.v1_19_R1),
    v1_19_1(4, NMSVersion.v1_19_R1),
    v1_19_2(5, NMSVersion.v1_19_R1),
    v1_19_3(6, NMSVersion.v1_19_R2),
    v1_19_4(7, NMSVersion.v1_19_R3),
    v1_20(8, NMSVersion.v1_20_R1),
    v1_20_1(9, NMSVersion.v1_20_R1),
    v1_20_2(10, NMSVersion.v1_20_R2),
    v1_20_3(11, NMSVersion.v1_20_R3),
    v1_20_4(12, NMSVersion.v1_20_R3),
    v1_20_5(13, NMSVersion.v1_20_R4),
    v1_20_6(14, NMSVersion.v1_20_R4),
    v1_21(15, NMSVersion.v1_21_R1),
    v1_21_1(16, NMSVersion.v1_21_R1),
    UNSUPPORTED(-1, NMSVersion.UNSUPPORTED);

    private static MinecraftVersion serverVersionSelected = null;

    private final int versionNumber;
    private final NMSVersion nmsVersion;

    MinecraftVersion(int versionNumber, NMSVersion nmsVersion) {
        this.versionNumber = versionNumber;
        if (nmsVersion == null) {
            this.nmsVersion = NMSVersion.UNSUPPORTED;
        } else {
            this.nmsVersion = nmsVersion;
        }
    }

    public NMSVersion getNMSVersion() {
        return nmsVersion;
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
    public boolean isAtLeast(MinecraftVersion otherVersion) {
        return versionNumber >= otherVersion.versionNumber;
    }

    /**
     * Checks if the current ServerVersion is less than the specified ServerVersion.
     *
     * @param otherVersion the ServerVersion to compare with the current version
     * @return true if the current version is less than the specified version, otherwise false
     */
    public boolean isLessThan(MinecraftVersion otherVersion) {
        return versionNumber < otherVersion.versionNumber;
    }

    /**
     * Converts a version string to a ServerVersion enum constant.
     *
     * @param version The version string to convert.
     * @return The corresponding ServerVersion enum constant.
     */
    public static MinecraftVersion fromString(String version) {
        try {
            return valueOf("v" + version.replace(".", "_"));
        } catch (IllegalArgumentException e) {
            return UNSUPPORTED;
        }
    }

    public static MinecraftVersion fromVersionNumber(int i) {
        for (MinecraftVersion srv : values()) {
            if (srv.versionNumber == i) return srv;
        }
        return UNSUPPORTED;
    }

    /**
     * Retrieves the version of the server.
     *
     * @return The server version.
     */
    public static MinecraftVersion getServerVersionSelected() {
        if (serverVersionSelected == null) {
            String versionName = Bukkit.getBukkitVersion().split("-")[0];
            serverVersionSelected = fromString(versionName);
        }
        return serverVersionSelected;
    }

    public static String getServerVersionBukkit() {
        return Bukkit.getBukkitVersion().split("-")[0];
    }

    public static MinecraftVersion getLastSupportedVersion() {
        return values()[values().length - 2];
    }

    public enum NMSVersion {
        v1_18_R2(0),
        v1_19_R1(1),
        v1_19_R2(2),
        v1_19_R3(3),
        v1_20_R1(4),
        v1_20_R2(5),
        v1_20_R3(6),
        v1_20_R4(7),
        v1_21_R1(8),
        UNSUPPORTED(-1);

        private final int version;

        NMSVersion(int version) {
            this.version = version;
        }

        public String getVersionName() {
            return name().replace("v", "");
        }

        public int getVersion() {
            return version;
        }
    }
}
