package dev.wuason.mechanics.compatibilities.plugins;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginCompatibility {
    private final String pluginName;
    private boolean enabled;
    private boolean loaded;

    public PluginCompatibility(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }

    public boolean isEnabled() {
        if (!enabled) {
            enabled = Bukkit.getPluginManager().isPluginEnabled(pluginName);
        }
        return enabled;
    }

    public boolean isLoaded() {
        if (!loaded) {
            loaded = Bukkit.getPluginManager().getPlugin(pluginName) != null;
        }
        return loaded;
    }

    public Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin(pluginName);
    }
}
