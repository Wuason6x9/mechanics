package dev.wuason.mechanics.compatibilities.adapter;

import dev.wuason.mechanics.compatibilities.adapter.Adapter;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Locale;

public abstract class Implementation {

    private String type;
    private boolean enabled = false;
    private String pluginName;

    public Implementation(String type, String pluginName){
        this.type = type.toUpperCase(Locale.ENGLISH);
        this.pluginName = pluginName;
        Adapter.getTypes().put(this.type,this);
    }


    public abstract ItemStack getAdapterItem(String id);

    public abstract String getAdapterID(ItemStack itemStack);
    public abstract String getAdapterID(Block block);
    public abstract boolean existItemAdapter(String id);


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getType() {
        return type;
    }

    public boolean isEnabled() {
        if(enabled == false) enabled = Bukkit.getPluginManager().isPluginEnabled(pluginName);
        return enabled;
    }

    public String getPluginName() {
        return pluginName;
    }
    public Plugin getPlugin(){
        return Bukkit.getPluginManager().getPlugin(pluginName);
    }
}
