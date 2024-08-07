package dev.wuason.mechanics.mechanics;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.library.classpath.MechanicClassLoader;
import dev.wuason.mechanics.utils.VersionDetector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class MechanicManager implements Listener {
    private final Mechanics core;
    private final ArrayList<MechanicAddon> addons = new ArrayList<>();
    private static final HashMap<MechanicAddon, MechanicClassLoader> classLoaders = new HashMap<>();

    public MechanicManager(Mechanics core) {
        this.core = core;
    }

    @EventHandler
    public void onEnable(PluginEnableEvent event) {
        if (event.getPlugin() instanceof MechanicAddon) {
            MechanicAddon addon = (MechanicAddon) event.getPlugin();
            addons.add(addon);
            addon.onVersion(VersionDetector.getServerVersion());
        }
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if(addons.contains(event.getPlugin())) {
            addons.remove(event.getPlugin());
        }
        if (classLoaders.containsKey(event.getPlugin())) {
            MechanicClassLoader classLoader = classLoaders.get(event.getPlugin());
            classLoader.close();
        }
    }

    public static void addClassLoader(MechanicAddon addon, MechanicClassLoader classLoader) {
        classLoaders.put(addon, classLoader);
    }
}
