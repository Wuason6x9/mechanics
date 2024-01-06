package dev.wuason.nms.wrappers;

import dev.wuason.nms.utils.VersionNmsDetector;

import java.lang.reflect.InvocationTargetException;

public class ServerNmsVersion {
    private static VersionWrapper versionWrapper = null;

    public ServerNmsVersion(){
        VersionNmsDetector.ServerVersion version = VersionNmsDetector.getServerVersion();
        try {
            versionWrapper = (VersionWrapper) Class.forName("dev.wuason.nms.nms_" + version.toString().replace("v","") + ".VersionWrapper_" + version.toString().replace("v","")).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public static VersionWrapper getVersionWrapper() {
        return versionWrapper;
    }
}
