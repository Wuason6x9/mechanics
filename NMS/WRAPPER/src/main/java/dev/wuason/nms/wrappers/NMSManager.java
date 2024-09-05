package dev.wuason.nms.wrappers;

import dev.wuason.mechanics.utils.VersionDetector;
import dev.wuason.nms.utils.VersionNMS;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;

public class NMSManager {
    private static VersionWrapper versionWrapper;

    public final static String CLASS_WRAPPER = "dev.wuason.nms.nms_{version}.VersionWrapper";

    public NMSManager(Plugin plugin) {
        if (versionWrapper != null) throw new IllegalStateException("NMSManager already initialized");
        VersionNMS.ServerVersionNMS version = VersionNMS.getServerVersion();
        try {
            versionWrapper = (VersionWrapper) Class.forName(CLASS_WRAPPER.replace("{version}", version.toString().replace("v", ""))).getDeclaredConstructors()[0].newInstance(plugin);
        } catch (InstantiationException | ClassNotFoundException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static VersionWrapper getVersionWrapper() {
        return versionWrapper;
    }

}
