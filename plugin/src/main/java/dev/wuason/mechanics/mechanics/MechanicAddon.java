package dev.wuason.mechanics.mechanics;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.utils.VersionDetector;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MechanicAddon extends JavaPlugin {

    private final String identifier;

    public MechanicAddon(String identifier) {
        this.identifier = identifier;
    }

    public void onVersion(VersionDetector.ServerVersion version) {
    }

    public String getIdentifier() {
        return identifier;
    }

    public Mechanics getMechanics() {
        return Mechanics.getInstance();
    }

    @Override
    public void onEnable() {
        onMechanicEnable();
    }

    @Override
    public void onDisable() {
        onMechanicDisable();
    }

    @Override
    public void onLoad() {
        onMechanicLoad();
    }

    public void onMechanicEnable() {
    }

    public void onMechanicDisable() {
    }

    public void onMechanicLoad() {
    }

}