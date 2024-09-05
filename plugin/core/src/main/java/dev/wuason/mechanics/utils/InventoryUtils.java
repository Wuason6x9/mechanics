package dev.wuason.mechanics.utils;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class InventoryUtils {
    /**
     * Checks if the given raw slot is within the boundaries of the inventory size.
     *
     * @param rawSlot       the raw slot number
     * @param inventorySize the size of the inventory
     * @return true if the raw slot is within the inventory size, false otherwise
     */
    public static boolean isOpenedInventory(int rawSlot, int inventorySize){
        return rawSlot < inventorySize;
    }
    /**
     * Determines if a given raw slot is within the boundaries of the inventory size.
     * @param rawSlot The raw slot to check.
     * @param inv The inventory to check against.
     * @return true if the raw slot is within the boundaries of the inventory size, false otherwise.
     */
    public static boolean isOpenedInventory(int rawSlot, Inventory inv){
        return isOpenedInventory(rawSlot, inv.getSize());
    }

    /**
     * Determines whether the given raw slot is part of an opened inventory.
     *
     * @param rawSlot The raw slot number.
     * @param inv The inventory type.
     * @return Returns the difference between the raw slot number and the default size of the inventory type.
     */
    public static int isOpenedInventory(int rawSlot, InventoryType inv){
        return rawSlot - inv.getDefaultSize();
    }

    /**
     * Checks if the given raw slot belongs to the player inventory.
     *
     * @param rawSlot the raw slot number
     * @param inv     the inventory to check against
     * @return true if the raw slot belongs to the player inventory, false otherwise
     */
    public static boolean isPlayerInventory(int rawSlot, Inventory inv){
        return rawSlot >= inv.getSize();
    }

    /**
     * Determines whether the given raw slot is part of the player's inventory.
     *
     * @param rawSlot The raw slot number.
     * @param inv The inventory type.
     * @return Returns true if the raw slot is within the boundaries of the player's inventory size, false otherwise.
     */
    public static boolean isPlayerInventory(int rawSlot, InventoryType inv){
        return rawSlot >= inv.getDefaultSize();
    }


}
