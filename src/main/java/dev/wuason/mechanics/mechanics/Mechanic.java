package dev.wuason.mechanics.mechanics;

import java.io.File;

public class Mechanic {
    private String addonMechanicId;
    private File addonMechanicFile;

    private String addonMechanicName;

    public Mechanic(String addonMechanicId, File addonMechanicFile,String addonMechanicName) {
        this.addonMechanicId = addonMechanicId;
        this.addonMechanicFile = addonMechanicFile;
        this.addonMechanicName = addonMechanicName;
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
}
