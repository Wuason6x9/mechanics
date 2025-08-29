package dev.wuason.nms.wrappers;

import dev.wuason.nms.utils.VersionNMS;

import java.lang.reflect.InvocationTargetException;

public class NMSManager {
    private static VersionWrapper versionWrapper = null;

    public NMSManager() {
        if (versionWrapper != null) return;
        VersionNMS.ServerVersionNMS version = VersionNMS.getServerVersion();
        try {
            versionWrapper = (VersionWrapper) Class.forName("dev.wuason.nms.nms_" + version.toString().replace("v", "") + ".VersionWrapper").getDeclaredConstructor().newInstance();
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public static VersionWrapper getVersionWrapper() {
        return versionWrapper;
    }

}
