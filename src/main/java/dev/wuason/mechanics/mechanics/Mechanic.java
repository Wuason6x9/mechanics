package dev.wuason.mechanics.mechanics;

import java.io.File;

public class Mechanic {
    private String addonMechanicId;
    private File addonMechanicFile;

    private String addonMechanicName;
    private String version;
    private String apiMcVersion;

    private File dirConfig;

    public Mechanic(String addonMechanicId, File addonMechanicFile,String addonMechanicName, String apiMcVersion, String version) {
        this.addonMechanicId = addonMechanicId;
        this.addonMechanicFile = addonMechanicFile;
        this.addonMechanicName = addonMechanicName;
        this.apiMcVersion = apiMcVersion;
        this.version = version;
    }

    public File getAddonMechanicFile() {
        return addonMechanicFile;
    }

    public String getAddonMechanicId() {
        return addonMechanicId;
    }

    public String getAddonMechanicName() {
        return addonMechanicName;
    }

    public String getVersion() {
        return version;
    }

    public String getApiMcVersion() {
        return apiMcVersion;
    }

    public File getDirConfig() {
        return dirConfig;
    }

    public void setDirConfig(File dirConfig) {
        this.dirConfig = dirConfig;
    }
}
