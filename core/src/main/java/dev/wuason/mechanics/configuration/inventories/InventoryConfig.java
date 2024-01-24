package dev.wuason.mechanics.configuration.inventories;

import dev.wuason.mechanics.compatibilities.adapter.Adapter;
import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import dev.wuason.mechanics.invmechanic.types.InvCustom;
import dev.wuason.mechanics.utils.AdventureUtils;
import dev.wuason.mechanics.utils.Utils;
import org.apache.commons.lang3.function.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class InventoryConfig {

    private final String id;
    private final ConfigurationSection section;
    private final InventoryConfigManager manager;
    private Consumer<InventoryConfig> onLoad;
    private TriConsumer<InventoryConfig, ConfigurationSection, ItemConfig> onItemLoad;
    private HashMap<String, Object> data = new HashMap<>();
    private final Function<InvCustom, Inventory> createInventoryFunction;
    private BiConsumer<ItemInterface, ItemConfig> itemBlockedConsumer;


    public InventoryConfig(ConfigurationSection section, InventoryConfigManager manager, Consumer<InventoryConfig> onLoad, TriConsumer<InventoryConfig, ConfigurationSection, ItemConfig> onItemLoad, @Nullable BiConsumer<ItemInterface, ItemConfig> itemBlockedConsumer) {
        createInventoryFunction = (invCustom) -> {
            String type = section.getString("type", "CHEST").toUpperCase(Locale.ENGLISH);
            InventoryType inventoryType = InventoryType.valueOf(type);
            Inventory inv = null;
            switch (inventoryType){
                case CHEST -> {
                    int size = (section.getInt("rows", 3) * 9);
                    inv = Bukkit.createInventory(invCustom, size, AdventureUtils.deserializeLegacy(section.getString("title", "Inventory"), null));
                }
                default -> {
                    inv = Bukkit.createInventory(invCustom, inventoryType, AdventureUtils.deserializeLegacy(section.getString("title", "Inventory"), null));
                }
            }

            invCustom.setDamageCancel(true);
            invCustom.setDamageCancel(true);
            invCustom.addClickEventsListeners(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

            return inv;
        };


        this.itemBlockedConsumer = itemBlockedConsumer;
        this.id = section.getName();
        this.section = section;
        this.manager = manager;
        this.onLoad = onLoad;
        this.onItemLoad = onItemLoad;
        load();
    }

    public InventoryConfig(ConfigurationSection section, InventoryConfigManager manager){
        createInventoryFunction = (invCustom) -> {
            String type = section.getString("type", "CHEST").toUpperCase(Locale.ENGLISH);
            InventoryType inventoryType = InventoryType.valueOf(type);
            Inventory inv = null;
            switch (inventoryType){
                case CHEST -> {
                    int size = (section.getInt("rows", 3) * 9);
                    inv = Bukkit.createInventory(invCustom, size, AdventureUtils.deserializeLegacy(section.getString("title", "Inventory"), null));
                }
                default -> {
                    inv = Bukkit.createInventory(invCustom, inventoryType, AdventureUtils.deserializeLegacy(section.getString("title", "Inventory"), null));
                }
            }
            invCustom.setDamageCancel(true);
            invCustom.setDamageCancel(true);
            invCustom.addClickEventsListeners(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

            return inv;
        };

        this.id = section.getName();
        this.section = section;
        this.manager = manager;
    }

    public void load() {
        ConfigurationSection itemsSection = this.section.getConfigurationSection("items");
        if(itemsSection != null){
            for(String id : itemsSection.getKeys(false)){
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(id);
                if(itemSection != null){
                    String actionId = itemSection.getString("id", "");
                    String itemId = itemSection.getString("item", "");
                    int amount = itemSection.getInt("amount", 1);
                    List<Integer> slots = Utils.configFill(itemSection.getStringList("slots"));
                    ItemConfig itemConfig = new ItemConfig(id, itemId, amount, slots, actionId);
                    if(itemConfig.getActionId().equalsIgnoreCase("blocked")){
                        ItemInterface item = new ItemInterface.Builder()
                                .setId(itemConfig.getId())
                                .setItemStack(Adapter.getInstance().getItemStack(itemConfig.getItemId()))
                                .setAmount(itemConfig.getAmount())
                                .setSlot(0)
                                .addData(itemConfig)
                                .onClick((e, invC) -> e.setCancelled(true))
                                .build();
                        if(this.itemBlockedConsumer != null) this.itemBlockedConsumer.accept(item, itemConfig);
                    }
                    if(this.onItemLoad != null) this.onItemLoad.accept(this, itemSection, itemConfig);
                }
            }
        }
        if(this.onLoad != null) this.onLoad.accept(this);
    }



    //****************** PUBLIC METHODS ******************//

    /**
     * Adds data to the inventory config.
     *
     * @param id The ID of the data.
     * @param object The object to be added as data.
     */
    public void addData(String id, Object object){
        this.data.put(id.toUpperCase(Locale.ENGLISH), object);
    }
    /**
     * Retrieves the data associated with the given ID.
     *
     * @param id The ID of the data to retrieve.
     * @return The data associated with the given ID, or null if the ID is not found.
     */
    public Object getData(String id){
        return this.data.get(id.toUpperCase(Locale.ENGLISH));
    }

    /**
     * Checks if the specified id exists in the inventory data.
     *
     * @param id The id to check for.
     * @return {@code true} if the id exists in the inventory data, {@code false} otherwise.
     */
    public boolean hasData(String id){
        return this.data.containsKey(id.toUpperCase(Locale.ENGLISH));
    }

    /**
     * Removes data from the inventory configuration.
     *
     * @param id The id of the data to be removed.
     */
    public void removeData(String id){
        this.data.remove(id.toUpperCase(Locale.ENGLISH));
    }

    /**
     * Sets the data for the InventoryConfig object.
     *
     * @param data A HashMap containing the data to be set. The keys in the HashMap represent the identifiers for the data, and the values represent the data itself.
     */
    public void setData(HashMap<String, Object> data){
        this.data = data;
    }

    //****************** SETTERS ******************//

    public void setOnLoad(Consumer<InventoryConfig> onLoad) {
        this.onLoad = onLoad;
    }

    public void setOnItemLoad(TriConsumer<InventoryConfig, ConfigurationSection, ItemConfig> onItemLoad) {
        this.onItemLoad = onItemLoad;
    }

    public void setItemBlockedConsumer(BiConsumer<ItemInterface, ItemConfig> itemBlockedConsumer) {
        this.itemBlockedConsumer = itemBlockedConsumer;
    }

    //****************** GETTERS ******************//

    public String getTitle(){
        return this.section.getString("title", "Inventory");
    }

    public int getRows(){
        return this.section.getInt("rows", 3);
    }

    public InventoryType getType(){
        return InventoryType.valueOf(this.section.getString("type", "CHEST").toUpperCase(Locale.ENGLISH));
    }

    public String getId() {
        return this.id;
    }

    public ConfigurationSection getSection() {
        return this.section;
    }

    public InventoryConfigManager getManager() {
        return this.manager;
    }

    public Consumer<InventoryConfig> getOnLoad() {
        return this.onLoad;
    }
    public HashMap<String, Object> getData() {
        return this.data;
    }

    public TriConsumer<InventoryConfig, ConfigurationSection, ItemConfig> getOnItemLoad() {
        return this.onItemLoad;
    }

    public Function<InvCustom, Inventory> getCreateInventoryFunction() {
        return this.createInventoryFunction;
    }

    public static class Builder {
        private String id;
        private ConfigurationSection section;
        private InventoryConfigManager manager;
        private Consumer<InventoryConfig> onLoad;
        private TriConsumer<InventoryConfig, ConfigurationSection, ItemConfig> onItemLoad;
        private BiConsumer<ItemInterface, ItemConfig> itemBlockedConsumer;
        private HashMap<String, Object> data = new HashMap<>();

        public Builder() {
        }

        public Builder addData(String id, Object object){
            this.data.put(id.toUpperCase(Locale.ENGLISH), object);
            return this;
        }

        public Builder setData(HashMap<String, Object> data){
            this.data = data;
            return this;
        }

        public Builder setId(String id) {
            this.id = id.toUpperCase(Locale.ENGLISH);
            return this;
        }

        public Builder setSection(ConfigurationSection section) {
            this.section = section;
            return this;
        }

        public Builder setManager(InventoryConfigManager manager) {
            this.manager = manager;
            return this;
        }

        public Builder onLoad(Consumer<InventoryConfig> onLoad) {
            this.onLoad = onLoad;
            return this;
        }

        public Builder onItemLoad(TriConsumer<InventoryConfig, ConfigurationSection, ItemConfig> onItemLoad) {
            this.onItemLoad = onItemLoad;
            return this;
        }
        public Builder setItemBlockedConsumer(BiConsumer<ItemInterface, ItemConfig> itemBlockedConsumer) {
            this.itemBlockedConsumer = itemBlockedConsumer;
            return this;
        }
        public String getId() {
            return this.id;
        }

        public ConfigurationSection getSection() {
            return this.section;
        }

        public InventoryConfigManager getManager() {
            return this.manager;
        }

        public Consumer<InventoryConfig> getOnLoad() {
            return this.onLoad;
        }

        public TriConsumer<InventoryConfig, ConfigurationSection, ItemConfig> getOnItemLoad() {
            return this.onItemLoad;
        }

        public HashMap<String, Object> getData() {
            return this.data;
        }

        public InventoryConfig build(){
            InventoryConfig inventoryConfig = null;
            if(this.onItemLoad == null && this.onLoad == null) inventoryConfig = new InventoryConfig(this.section, this.manager);
            else inventoryConfig = new InventoryConfig(this.section, this.manager, this.onLoad, this.onItemLoad, this.itemBlockedConsumer);
            inventoryConfig.setData(this.data);
            return inventoryConfig;
        }
    }

}
