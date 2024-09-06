package dev.wuason.mechanics.mechanics;

import dev.wuason.libs.bstats.Metrics;
import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.library.classpath.MechanicClassLoader;
import dev.wuason.mechanics.utils.version.MinecraftVersion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MechanicManager implements Listener {
    private final Mechanics core;
    private final ArrayList<MechanicAddon> addons = new ArrayList<>();
    private static final HashMap<MechanicAddon, List<MechanicClassLoader>> classLoaders = new HashMap<>();

    public MechanicManager(Mechanics core) {
        this.core = core;
    }

    @EventHandler
    public void onEnable(PluginEnableEvent event) {
        if (!(event.getPlugin() instanceof Mechanics) && event.getPlugin() instanceof MechanicAddon addon) {
            addons.add(addon);
            addon.onVersion(MinecraftVersion.getServerVersionSelected());
            if(addon.getMetricsPluginId() != -1) {
                Metrics metrics = new Metrics(addon, addon.getMetricsPluginId());
                addon.onMetricsEnable(metrics);
            }

        }
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (event.getPlugin() instanceof Mechanics) return;
        if(addons.contains(event.getPlugin())) {
            addons.remove(event.getPlugin());
        }
        if (classLoaders.containsKey(event.getPlugin())) {
            List<MechanicClassLoader> classLoaders = MechanicManager.classLoaders.get(event.getPlugin());
            for (MechanicClassLoader classLoader : classLoaders) {
                classLoader.close();
            }
        }
    }

    public static void addClassLoader(MechanicAddon addon, MechanicClassLoader classLoader) {
        if (!classLoaders.containsKey(addon)) {
            classLoaders.put(addon, new ArrayList<>());
        }
        classLoaders.get(addon).add(classLoader);
    }

    public void onStop() {
        for (MechanicAddon addon : addons) {
            if (classLoaders.containsKey(addon)) {
                List<MechanicClassLoader> classLoaders = MechanicManager.classLoaders.get(addon);
                for (MechanicClassLoader classLoader : classLoaders) {
                    classLoader.close();
                }
            }
        }
    }
}
