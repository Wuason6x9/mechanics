package dev.wuason.mechanics.inventory.items;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.inventory.InvCustom;
import dev.wuason.mechanics.items.ItemBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ItemInterface {

    private final int slot;
    private final ItemStack itemStack;
    private final String id;
    private final String name;
    private final long creationTime = System.currentTimeMillis();

    public static final NamespacedKey NAMESPACED_KEY_ITEM_INTERFACE = new NamespacedKey(Mechanics.getInstance(), "icm_item_interface");

    public ItemInterface(int slot, ItemStack itemStack, String name) {
        this(slot, itemStack, null, name);
    }
    public ItemInterface(int slot, ItemStack itemStack) {
        this(slot, itemStack, null,"");
    }

    public ItemInterface(int slot, String id, ItemStack itemStack) {
        this(slot, itemStack, id, "");
    }

    public ItemInterface(int slot, ItemStack itemStack, String id, String name) {
        Objects.requireNonNull(slot, "Slot cannot be null");
        this.slot = slot;
        this.itemStack = itemStack;
        this.id = id == null ? UUID.randomUUID().toString().toUpperCase(Locale.ENGLISH) : id.toUpperCase(Locale.ENGLISH);
        this.name = name;
    }

    @NotNull
    public int getSlot() {
        return slot;
    }

    @NotNull
    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    @NotNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void onClick(InventoryClickEvent event, InvCustom invCustom) {};

    public ItemStack getItemModified() {
        ItemStack itemStack = getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(NAMESPACED_KEY_ITEM_INTERFACE, PersistentDataType.STRING, id);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static class Builder {
        private final int slot;
        private final ItemStack itemStack;
        private String id;
        private String name;
        private BiConsumer<InventoryClickEvent, InvCustom> onClick;

        public Builder(int slot, ItemStack itemStack) {
            this.slot = slot;
            this.itemStack = itemStack;
        }

        public Builder setAmount(int amount) {
            this.itemStack.setAmount(amount);
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder onClick(BiConsumer<InventoryClickEvent, InvCustom> onClick) {
            this.onClick = onClick;
            return this;
        }

        public ItemInterface build() {
            return new ItemInterface(slot, itemStack, id, name) {
                @Override
                public void onClick(InventoryClickEvent event, InvCustom inventoryCustom) {
                    if(onClick != null) onClick.accept(event, inventoryCustom);
                }
            };
        }
    }

    public static boolean isItemInterface(ItemStack itemStack) {
        if(itemStack == null || !itemStack.hasItemMeta()) return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.getPersistentDataContainer().has(NAMESPACED_KEY_ITEM_INTERFACE, PersistentDataType.STRING);
    }

    public static String getId(ItemStack itemStack) {
        if(!isItemInterface(itemStack)) return null;
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.getPersistentDataContainer().get(NAMESPACED_KEY_ITEM_INTERFACE, PersistentDataType.STRING);
    }

    public static ItemStack apply(ItemStack itemStack, String id) {
        if (!itemStack.hasItemMeta()) return itemStack;
        itemStack.editMeta(meta -> {
            meta.getPersistentDataContainer().set(NAMESPACED_KEY_ITEM_INTERFACE, PersistentDataType.STRING, id);
        });
        return itemStack;
    }

}
