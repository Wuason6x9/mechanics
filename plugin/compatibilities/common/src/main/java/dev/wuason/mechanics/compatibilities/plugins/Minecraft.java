package dev.wuason.mechanics.compatibilities.plugins;

import org.bukkit.plugin.Plugin;

public class Minecraft extends PluginCompatibility {

        public static final Minecraft I = new Minecraft();

        private Minecraft() {
            super("Minecraft");
        }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public Plugin getPlugin() {
        return null;
    }
}
