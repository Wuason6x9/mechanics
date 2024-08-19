package dev.wuason.mechanics.mechanics;

import dev.wuason.libs.bstats.Metrics;
import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.utils.VersionDetector;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public abstract class MechanicAddon extends JavaPlugin {

    private final String identifier;
    private int metricsPluginId = -1;

    public MechanicAddon(String identifier) {
        this.identifier = identifier;
    }

    public MechanicAddon() {
        this.identifier = this.getDescription().getName().toLowerCase(Locale.ENGLISH);
    }

    public MechanicAddon(String identifier, int metricsPluginId) {
        this.identifier = identifier;
        this.metricsPluginId = metricsPluginId;
    }

    public MechanicAddon(int metricsPluginId) {
        this.identifier = this.getDescription().getName().toLowerCase(Locale.ENGLISH);
        this.metricsPluginId = metricsPluginId;
    }

    public void onVersion(VersionDetector.ServerVersion version) {
    }

    public void onMetricsEnable(Metrics metrics) {
    }

    public String getIdentifier() {
        return identifier;
    }

    public Mechanics getMechanics() {
        return this instanceof Mechanics ? (Mechanics) this : Mechanics.getInstance();
    }

    @Override
    public void onEnable() {
        if(!getMechanics().isEnabled()) {
            getMechanics().getLogger().severe("Mechanics is not enabled, disabling " + getDescription().getName());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
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

    public void setMetricsPluginId(int metricsPluginId) {
        this.metricsPluginId = metricsPluginId;
    }

    public int getMetricsPluginId() {
        return metricsPluginId;
    }

}