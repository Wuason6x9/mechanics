package dev.wuason.mechanics.compatibilities.adapter.plugins;

import dev.wuason.mechanics.compatibilities.plugins.PluginCompatibility;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public abstract class ImplAdapter {
    private final PluginCompatibility pluginCompatibility;
    private final String type;
    private boolean checked = false;
    private boolean enabled = false;

    ImplAdapter(PluginCompatibility pluginCompatibility, String type) {
        this.pluginCompatibility = pluginCompatibility;
        this.type = type.toLowerCase(Locale.ENGLISH);
    }

    public PluginCompatibility getPluginCompatibility() {
        return pluginCompatibility;
    }

    public String getType() {
        return type;
    }

    public abstract ItemStack getAdapterItem(String id);

    public abstract String getAdapterId(ItemStack itemStack);

    public abstract String getAdapterId(Block block);

    public abstract String getAdapterId(Entity entity);

    public abstract boolean existItemAdapter(String id);

    public void onEnable() {
    }

    String convert(String id) {
        return getType() + ":" + id;
    }

    boolean check() {
        if (!enabled && checked) return false;
        else if (enabled) return true;
        checked = true;
        if (pluginCompatibility.isLoaded()) {
            enabled = true;
            onEnable();
        }
        return enabled;
    }


}
