package dev.wuason.mechanics.invmechanic.items;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.invmechanic.types.InvCustom;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;

public class ItemInterface {
    private final int slot;
    private final ItemStack itemStack;
    private final String id;
    private String name;
    private ArrayList<String> data = new ArrayList<>();

    public ItemInterface(int slot, ItemStack itemStack, String name) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }
    public ItemInterface(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.id = UUID.randomUUID().toString();
    }
    public ItemInterface(int slot, ItemStack itemStack, String name, ArrayList<String> data) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.data = data;
    }
    public ItemInterface(int slot, ItemStack itemStack, String id, String name) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.id = id;
        this.name = name;
    }
    public ItemInterface(int slot, ItemStack itemStack, String id, String name, ArrayList<String> data) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.id = id;
        this.name = name;
        this.data = data;
    }

    public ArrayList<String> getData() {
        return data;
    }
    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getId() {
        return id;
    }

    public void onClick(InventoryClickEvent event, InvCustom inventoryCustom) {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItemModified(){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Mechanics.getInstance(), InvCustom.NAMESPACE_ITEM_INTERFACE_PREFIX), PersistentDataType.STRING, id);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
