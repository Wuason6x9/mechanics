package dev.wuason.mechanics.compatibilities.plugins;

public class Oraxen extends PluginCompatibility {

    public static final Oraxen I = new Oraxen();

    private Oraxen() {
        super("Oraxen");
    }

    public boolean isOraxen2() {
        if (!isLoaded()) return false;
        return getPlugin().getDescription().getVersion().startsWith("2");
    }
}
