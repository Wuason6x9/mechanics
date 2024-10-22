package dev.wuason.mechanics.utils.version;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.nms.NMSManager;
import dev.wuason.mechanics.nms.wrappers.VersionWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

/**
 * Enumerates supported Minecraft versions, each associated with a
 * version number and an NMS (Net Minecraft Server) version.
 */
public enum MinecraftVersion {
    /**
     * Enum constant representing a specific version, in this case version 1.18.2.
     * It associates the version with an identifier and corresponding NMS version.
     */
    V1_18_2(2, NMSVersion.V1_18_R2),
    /**
     * Enum constant representing version 1.19.
     * It is mapped to value 3 and associated with the NMS version V1_19_R1.
     */
    V1_19(3, NMSVersion.V1_19_R1),
    /**
     * Enumeration constant representing version 1.19.1.
     * This constant is used to specify a specific version of the software.
     * The first parameter indicates the version number and the second parameter specifies
     * the corresponding NMS (Net Minecraft Server) version.
     */
    V1_19_1(4, NMSVersion.V1_19_R1),
    /**
     * Enumeration constant representing version 1.19.2.
     *
     * The value associated with this constant is 5, and it maps to the
     * corresponding NMS (Net Minecraft Server) version V1_19_R1.
     */
    V1_19_2(5, NMSVersion.V1_19_R1),
    /**
     * Represents a specific version (1.19.3) with its designated numerical identifier (6)
     * and its corresponding NMS (Net Minecraft Server) version (V1_19_R2).
     * This variable is typically used for version control and compatibility checks within
     * the application.
     */
    V1_19_3(6, NMSVersion.V1_19_R2),
    /**
     * Enumeration constant representing version 1.19.4.
     * This constant is used to identify and manage version-specific logic and behaviors within the application.
     * The associated numeric identifier is 7, and the corresponding NMS (Net Minecraft Server) version is V1_19_R3.
     */
    V1_19_4(7, NMSVersion.V1_19_R3),
    /**
     * Enum constant representing the specific version 1.20.
     * This constant is used to denote compatibility with Minecraft version 1.20.
     *
     * @param value the numeric representation of the version.
     * @param nmsVersion the specific NMS (Net Minecraft Server) version associated with this Minecraft version.
     */
    V1_20(8, NMSVersion.V1_20_R1),
    /**
     * Represents the version constant for Minecraft 1.20.1.
     *
     * This version corresponds to the network protocol version 9 and is associated
     * with the NMS (Net Minecraft Server) version V1_20_R1.
     *
     * The versioning is utilized for compatibility and functionality mapping within
     * the application to ensure proper interfacing with Minecraft server versions.
     */
    V1_20_1(9, NMSVersion.V1_20_R1),
    /**
     * Enumeration constant representing the specific version V1_20_2.
     * It is associated with the value 10 and maps to the NMSVersion V1_20_R2.
     */
    V1_20_2(10, NMSVersion.V1_20_R2),
    /**
     * Enumeration constant representing version 1.20.3.
     * It is associated with an internal version identifier of 11 and a NMS version of V1_20_R3.
     */
    V1_20_3(11, NMSVersion.V1_20_R3),
    /**
     * An enum constant representing the version 1.20.4 with an associated version code and NMS (Net Minecraft Server) version.
     * This specific version is represented by the integer 12 and corresponds to the NMS version V1_20_R3.
     *
     * @param versionCode An integer representing the code of the version.
     * @param nmsVersion  An instance of NMSVersion associated with this specific game version.
     */
    V1_20_4(12, NMSVersion.V1_20_R3),
    /**
     * Represents a specific version identifier (V1_20_5) that is associated with
     * a numerical value and an NMS (Net Minecraft Server) version.
     * The numerical value in this context is 13.
     * The NMS version associated with this identifier is V1_20_R4.
     */
    V1_20_5(13, NMSVersion.V1_20_R4),
    /**
     * A constant representing a specific version of a system or software.
     *
     * V1_20_6 is associated with the version number 14 and maps to the
     * NMSVersion enum value V1_20_R4.
     */
    V1_20_6(14, NMSVersion.V1_20_R4),
    /**
     * Represents the version 1.21 of the application.
     * This version is identified by the integer code 15
     * and corresponds to the enumerated NMS version V1_21_R1.
     */
    V1_21(15, NMSVersion.V1_21_R1),
    /**
     * Represents version 1.21.1 of the software with an associated version
     * number of 16 and specific NMS (Net Minecraft Server) version V1_21_R1.
     *
     * This constant is used to identify and handle features or
     * functionalities specific to version 1.21.1 within the codebase.
     */
    V1_21_1(16, NMSVersion.V1_21_R1),
    /**
     * A constant representing an unsupported NMS (Net Minecraft Server) version.
     * The value -1 signifies an unsupported version, and it is associated with
     * the NMSVersion.UNSUPPORTED enumeration which indicates versions of Minecraft
     * that are not supported by the system.
     *
     * @param code the code representing the unsupported version, which is -1.
     * @param version the NMS version enumeration indicating unsupported status.
     */
    UNSUPPORTED(-1, NMSVersion.UNSUPPORTED);

    /**
     * Represents the specific version of the Minecraft server that has been selected.
     * This variable is used to store the selected Minecraft version to ensure compatibility
     * and proper configuration of the server.
     *
     * It is initialized to null, indicating that no version has been selected upon
     * initialization.
     */
    private static MinecraftVersion serverVersionSelected = null;

    /**
     * The version number of the current instance.
     * This variable is used to track the version of an object,
     * typically for purposes such as version control, data compatibility checks,
     * and to maintain state consistency across different instances.
     */
    private final int versionNumber;
    /**
     * The version of the Network Management System (NMS) being used.
     * This variable stores an instance of NMSVersion which provides
     * specific information and functionalities related to that version.
     * Since this is a final variable, once assigned, its reference cannot be changed.
     */
    private final NMSVersion nmsVersion;

    /**
     * Initializes a new instance of the MinecraftVersion class with the specified version number
     * and NMS version.
     *
     * @param versionNumber The version number of Minecraft.
     * @param nmsVersion The NMS version associated with this Minecraft version. If null,
     *                   the NMS version will be set to NMSVersion.UNSUPPORTED.
     */
    MinecraftVersion(int versionNumber, NMSVersion nmsVersion) {
        this.versionNumber = versionNumber;
        if (nmsVersion == null) {
            this.nmsVersion = NMSVersion.UNSUPPORTED;
        } else {
            this.nmsVersion = nmsVersion;
        }
    }

    /**
     * Retrieves the current NMS (Net Minecraft Server) version.
     *
     * @return the NMSVersion representing the current version.
     */
    public NMSVersion getNMSVersion() {
        return nmsVersion;
    }

    /**
     * Retrieves the version number.
     *
     * @return the current version number as an integer.
     */
    public int getVersionNumber() {
        return versionNumber;
    }

    /**
     * Retrieves the version name of the current enum constant.
     * The version name is derived by removing the "V" character and replacing underscores with dots.
     *
     * @return A String representing the version name of the enum constant, with "V" removed and underscores replaced by dots.
     */
    public String getVersionName() {
        return name().replace("V", "").replace("_", ".");
    }

    /**
     * Checks if the current Minecraft version is at least the specified version.
     *
     * @param otherVersion The MinecraftVersion object to compare against.
     * @return true if the current version number is greater than or equal to the otherVersion's version number, false otherwise.
     */
    public boolean isAtLeast(MinecraftVersion otherVersion) {
        return versionNumber >= otherVersion.versionNumber;
    }

    /**
     * Compares the current Minecraft version with another version to determine if it is less than the other version.
     *
     * @param otherVersion The MinecraftVersion object to compare with.
     * @return true if the current version is less than the other version; false otherwise.
     */
    public boolean isLessThan(MinecraftVersion otherVersion) {
        return versionNumber < otherVersion.versionNumber;
    }

    /**
     * Compares the current MinecraftVersion object with another MinecraftVersion object.
     *
     * @param otherVersion the MinecraftVersion object to compare against.
     * @return true if the current version number is greater than the specified version number, false otherwise.
     */
    public boolean isGreaterThan(MinecraftVersion otherVersion) {
        return versionNumber > otherVersion.versionNumber;
    }

    /**
     * Compares the current version with the specified version to check if the current version is at most
     * the specified version.
     *
     * @param otherVersion the MinecraftVersion instance to compare with the current version.
     * @return true if the current version is less than or equal to the specified version, false otherwise.
     */
    public boolean isAtMost(MinecraftVersion otherVersion) {
        return versionNumber <= otherVersion.versionNumber;
    }

    /**
     * Converts a string representation of a Minecraft version into a MinecraftVersion enum.
     *
     * @param version the string representation of the Minecraft version (e.g., "1.16.4").
     * @return the corresponding MinecraftVersion enum if it exists, else returns UNSUPPORTED.
     */
    public static MinecraftVersion fromString(String version) {
        try {
            return valueOf("V" + version.replace(".", "_"));
        } catch (IllegalArgumentException e) {
            return UNSUPPORTED;
        }
    }

    /**
     * Returns the MinecraftVersion corresponding to the specified version number.
     *
     * @param i the version number of the Minecraft version to be retrieved.
     * @return the MinecraftVersion that matches the given version number,
     *         or UNSUPPORTED if no matching version is found.
     */
    public static MinecraftVersion fromVersionNumber(int i) {
        for (MinecraftVersion srv : values()) {
            if (srv.versionNumber == i) return srv;
        }
        return UNSUPPORTED;
    }

    /**
     * Retrieves the selected server version. If it has not been selected yet,
     * it determines the version from Bukkit's version string and sets it.
     *
     * @return The selected Minecraft server version.
     */
    public static MinecraftVersion getServerVersionSelected() {
        if (serverVersionSelected == null) {
            String versionName = Bukkit.getBukkitVersion().split("-")[0];
            serverVersionSelected = fromString(versionName);
        }
        return serverVersionSelected;
    }

    /**
     * Retrieves the server version of Bukkit.
     *
     * @return The server version string from Bukkit.
     */
    public static String getServerVersionBukkit() {
        return Bukkit.getBukkitVersion().split("-")[0];
    }

    /**
     * Retrieves the last supported version of Minecraft from an array of version values.
     *
     * @return The second to last Minecraft version indicating the last officially supported version.
     */
    public static MinecraftVersion getLastSupportedVersion() {
        return values()[values().length - 2];
    }

    /**
     * The NMSVersion enum defines various versions of Minecraft NMS (Net Minecraft Server).
     * Each enum constant is associated with an integer value that signifies the version.
     */
    public enum NMSVersion {
        /**
         * Represents the Minecraft NMS version 1.18.2.
         * This enum constant corresponds to an integer value that signifies the version.
         */
        V1_18_R2(0),
        /**
         * Represents the Minecraft Net Minecraft Server (NMS) version 1.19.R1.
         * This version is associated with integer value 1.
         */
        V1_19_R1(1),
        /**
         * Represents the NMSVersion for Minecraft version 1.19 revision 2.
         */
        V1_19_R2(2),
        /**
         * Represents the Minecraft NMS version 1.19 Release 3.
         * This enum constant is associated with the integer value 3.
         */
        V1_19_R3(3),
        /**
         * Represents the Minecraft Net Minecraft Server version 1.20 release 1.
         * This version is associated with the integer value 4.
         */
        V1_20_R1(4),
        /**
         * Represents the Minecraft NMS (Net Minecraft Server) version 1.20 R2 (revision 2).
         * It is associated with the integer value 5.
         */
        V1_20_R2(5),
        /**
         * Represents the Minecraft NMS version 1.20 Release 3.
         * This version is associated with the integer value 6.
         */
        V1_20_R3(6),
        /**
         * Represents the Minecraft NMS version 1.20 release 4.
         * This enum constant is associated with the integer value 7.
         */
        V1_20_R4(7),
        /**
         * Minecraft NMS version 1.21 Release 1.
         */
        V1_21_R1(8),
        /**
         * Indicates an unsupported NMS version.
         * This version is used as a fallback when the actual version cannot be determined or is not recognized.
         */
        UNSUPPORTED(-1);

        /**
         * Represents the integer value associated with a specific Minecraft NMS (Net Minecraft Server) version.
         */
        private final int version;

        /**
         * Constructs an instance of NMSVersion with the specified version.
         *
         * @param version the integer value representing the version of the Minecraft NMS
         */
        NMSVersion(int version) {
            this.version = version;
        }

        /**
         * Checks if the current NMSVersion is supported.
         *
         * @return true if the current version is not UNSUPPORTED; false otherwise.
         */
        public boolean isSupported() {
            return this != UNSUPPORTED;
        }

        /**
         * Determines if the current NMS version is at least the specified version.
         *
         * @param otherVersion the NMSVersion to compare against
         * @return true if the current version is greater than or equal to the other version; false otherwise
         */
        public boolean isAtLeast(NMSVersion otherVersion) {
            return version >= otherVersion.version;
        }

        /**
         * Determines if the current version is less than the specified version.
         *
         * @param otherVersion the version to compare with the current version
         * @return true if the current version is less than the specified version, false otherwise
         */
        public boolean isLessThan(NMSVersion otherVersion) {
            return version < otherVersion.version;
        }

        /**
         * Compares this NMSVersion object with another to determine if it is greater.
         *
         * @param otherVersion the NMSVersion to compare with this version
         * @return true if this version is greater than the specified version, false otherwise
         */
        public boolean isGreaterThan(NMSVersion otherVersion) {
            return version > otherVersion.version;
        }

        /**
         * Determines if the current NMSVersion is at most the specified NMSVersion.
         *
         * @param otherVersion the other NMSVersion to compare against
         * @return true if the current NMSVersion is less than or equal to the specified NMSVersion; false otherwise
         */
        public boolean isAtMost(NMSVersion otherVersion) {
            return version <= otherVersion.version;
        }

        /**
         * Retrieves the version name of the enum constant by removing the
         * leading 'V' character from its name.
         *
         * @return the version name as a string without the leading 'V'.
         */
        public String getVersionName() {
            return name().replace("V", "");
        }

        /**
         * Gets the integer value associated with the version.
         *
         * @return the version number as an integer.
         */
        public int getVersion() {
            return version;
        }
    }
}
