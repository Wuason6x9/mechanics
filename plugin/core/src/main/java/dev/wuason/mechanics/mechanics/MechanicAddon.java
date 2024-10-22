package dev.wuason.mechanics.mechanics;

import dev.wuason.libs.bstats.Metrics;
import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.utils.version.MinecraftVersion;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

/**
 * Represents an abstract class for creating addons that extend the functionality of the Mechanics plugin.
 * This class provides methods for addon management, version handling, and metrics integration.
 */
public abstract class MechanicAddon extends JavaPlugin {

    /**
     * The unique identifier for this MechanicAddon instance.
     * This identifier is typically set to the plugin's name in lowercase.
     */
    private final String identifier;
    /**
     * The unique identifier for the metrics plugin associated with this addon.
     * Initialized to -1 to indicate that no metrics plugin ID is set.
     */
    private int metricsPluginId = -1;

    /**
     * Constructor for creating a new instance of MechanicAddon with a given identifier.
     *
     * @param identifier the unique identifier for this addon
     */
    public MechanicAddon(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Default constructor for the MechanicAddon class. This constructor sets the identifier
     * for the addon based on the name of the plugin, converting it to lowercase.
     */
    public MechanicAddon() {
        this.identifier = this.getDescription().getName().toLowerCase(Locale.ENGLISH);
    }

    /**
     * Constructs a new MechanicAddon with the specified identifier and metrics plugin ID.
     *
     * @param identifier the unique identifier for the MechanicAddon
     * @param metricsPluginId the ID of the metrics plugin to associate with this addon
     */
    public MechanicAddon(String identifier, int metricsPluginId) {
        this.identifier = identifier;
        this.metricsPluginId = metricsPluginId;
    }

    /**
     * Constructs a MechanicAddon instance with a specific metrics plugin ID.
     *
     * @param metricsPluginId the ID of the metrics plugin to associate with this addon
     */
    public MechanicAddon(int metricsPluginId) {
        this.identifier = this.getDescription().getName().toLowerCase(Locale.ENGLISH);
        this.metricsPluginId = metricsPluginId;
    }

    /**
     * Handles actions to be performed based on the specified Minecraft server version.
     *
     * @param version the Minecraft version of the server
     */
    public void onVersion(MinecraftVersion version) {
    }

    /**
     * Callback method that is called to enable metrics for the plugin.
     *
     * @param metrics The Metrics instance used to collect and send data.
     */
    public void onMetricsEnable(Metrics metrics) {
    }

    /**
     * Retrieves the unique identifier associated with this MechanicAddon.
     *
     * @return the identifier as a String.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Retrieves the Mechanics instance associated with this addon.
     *
     * @return the Mechanics instance if this is an instance of Mechanics, otherwise returns the
     *         singleton instance of Mechanics.
     */
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

    /**
     * Sets the identifier for the metrics plugin.
     *
     * @param metricsPluginId the ID of the metrics plugin to set
     */
    public void setMetricsPluginId(int metricsPluginId) {
        this.metricsPluginId = metricsPluginId;
    }

    /**
     * Retrieves the unique identifier for the metrics plugin associated with this addon.
     *
     * @return the metrics plugin ID, or -1 if no metrics plugin ID is set.
     */
    public int getMetricsPluginId() {
        return metricsPluginId;
    }

}