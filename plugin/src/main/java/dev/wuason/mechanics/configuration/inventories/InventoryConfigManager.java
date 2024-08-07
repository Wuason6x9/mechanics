package dev.wuason.mechanics.configuration.inventories;

import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import org.apache.commons.lang3.function.TriConsumer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class InventoryConfigManager {
    private final MechanicAddon core;
    private final HashMap<String, ConfigurationSection> inventories = new HashMap<>();
    private final File directory;

    /**
     * The InventoryConfigManager class manages the inventory configurations for a MechanicAddon.
     *
     * @param core The MechanicAddon implementation that this manager is associated with.
     * @param directory The directory where the inventory configuration files are located. Can be null.
     */
    public InventoryConfigManager(@NotNull MechanicAddon core, @Nullable File directory) {
        this.core = core;
        this.directory = directory;
    }
    /**
     * The InventoryConfigManager class manages the inventory configurations for a MechanicAddon.
     *
     * @param core The MechanicAddon implementation that this manager is associated with.
     */
    public InventoryConfigManager(@NotNull MechanicAddon core) {
        this(core, null);
    }



    public void load(){
        load(this.directory);
    }

    public void load(File dir){
        inventories.clear();
        dir.mkdirs();
        File[] files = Arrays.stream(dir.listFiles()).filter(f -> {

            if(f.getName().contains(".yml")) return true;

            return false;

        }).toArray(File[]::new);
        if(files == null) return;

        for(File file : files){

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            ConfigurationSection sectionInventories = config.getConfigurationSection("inventories");

            if(sectionInventories != null){
                for(String key : sectionInventories.getKeys(false)){

                    ConfigurationSection sectionInventory = sectionInventories.getConfigurationSection(key);

                    if(sectionInventory != null){
                        inventories.put(key.toUpperCase(Locale.ENGLISH), sectionInventory);
                    }

                }
            }
        }
    }

    /**
     * Creates an InventoryConfig object based on the specified id, onLoad consumer, and onItemLoad tri-consumer.
     *
     * @param id The id of the inventory configuration.
     * @param onLoad A consumer to be called when the inventory configuration is loaded.
     * @param onItemLoad A tri-consumer to be called when an item in the inventory configuration is loaded.
     * @return The created InventoryConfig object, or null if the id is not found in the inventories map.
     */
    public InventoryConfig createInventoryConfig(String id, Consumer<InventoryConfig> onLoad, TriConsumer<InventoryConfig, ConfigurationSection, ItemConfig> onItemLoad, @Nullable BiConsumer<ItemInterface, ItemConfig> itemBlockedConsumer){
        if(inventories.containsKey(id.toUpperCase(Locale.ENGLISH))){
            return new InventoryConfig(inventories.get(id.toUpperCase(Locale.ENGLISH)), this, onLoad, onItemLoad, itemBlockedConsumer);
        }
        return null;
    }

    //bybuilder
    public InventoryConfig createInventoryConfig(Consumer<InventoryConfig.Builder> invBuilder){
        InventoryConfig.Builder builder = new InventoryConfig.Builder();
        invBuilder.accept(builder);
        builder.setManager(this);
        ConfigurationSection section = inventories.get(builder.getId());
        if(section == null) return null;
        builder.setSection(section);
        InventoryConfig inv = builder.build();
        return inv;
    }

    public boolean existInventoryConfig(String id){
        return inventories.containsKey(id.toUpperCase(Locale.ENGLISH));
    }

    //********* Getters *********//

    public MechanicAddon getCore() {
        return this.core;
    }

    public HashMap<String, ConfigurationSection> getInventories()    {
        return this.inventories;
    }

    public File getDirectory() {
        return this.directory;
    }

    private Plugin getPlugin() {
        return (Plugin) this.core;
    }
}
