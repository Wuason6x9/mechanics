package dev.wuason.mechanics.nms;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.nms.wrappers.VersionWrapper;
import dev.wuason.mechanics.utils.version.MinecraftVersion;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;

public class NMSManager {
    private static VersionWrapper versionWrapper;

    public final static String CLASS_WRAPPER = "dev.wuason.mechanics.nms.v{version}.Version";

    public NMSManager(Mechanics plugin) {
        if (versionWrapper != null) throw new IllegalStateException("NMSManager already initialized");
        String nms = MinecraftVersion.getServerVersionSelected().getNMSVersion().getVersionName();
        try {
            versionWrapper = (VersionWrapper) Class.forName(CLASS_WRAPPER.replace("{version}", nms)).getDeclaredConstructors()[0].newInstance(plugin);
        } catch (InstantiationException | ClassNotFoundException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public static VersionWrapper getVersionWrapper() {
        return versionWrapper;
    }

}
