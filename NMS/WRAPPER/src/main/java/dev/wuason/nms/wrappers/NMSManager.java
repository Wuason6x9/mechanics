package dev.wuason.nms.wrappers;

import dev.wuason.mechanics.utils.VersionDetector;
import dev.wuason.nms.utils.VersionNMS;

import java.lang.reflect.InvocationTargetException;

public class NMSManager {
    private static VersionWrapper versionWrapper = null;

    public NMSManager(){
        if(versionWrapper != null) return;
        VersionNMS.ServerVersionNMS version = VersionNMS.getServerVersion();
        if (version == VersionNMS.ServerVersionNMS.PAPER_CUSTOM) {
            VersionDetector.ServerVersion serverVersion = VersionDetector.getServerVersion();
            version = VersionNMS.ServerVersionNMS.getServerVersion(serverVersion);
        }
        try {
            versionWrapper = (VersionWrapper) Class.forName("dev.wuason.nms.nms_" + version.toString().replace("v","") + ".VersionWrapper").getDeclaredConstructor().newInstance();
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public static VersionWrapper getVersionWrapper() {
        return versionWrapper;
    }

    public static String getCraftBukkitClassRoute(String className){
        return "org.bukkit.craftbukkit." + VersionNMS.getServerVersion().name() + "." + className;
    }

    public static Class<?> getCraftBukkitClass(String className){
        try {
            return Class.forName(getCraftBukkitClassRoute(className));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
