package dev.wuason.mechanics.utils;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class StorageUtils {
    /**
     * Adds an item to the player's inventory. If the inventory is full and there are remaining items, they will be dropped as items on the ground at the player's location.
     *
     * @param player The player whose inventory to add the item to.
     * @param itemStack The item to add to the inventory.
     */
    public static void addItemToInventoryOrDrop(Player player, ItemStack... itemStack) {
        itemStack = Arrays.stream(itemStack).filter(Objects::nonNull).toArray(ItemStack[]::new);
        HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(itemStack);
        if (!remainingItems.isEmpty()) {
            for (ItemStack remainingItem : remainingItems.values()) {
                Location dropLocation = player.getLocation();
                Item droppedItem = dropLocation.getWorld().dropItem(dropLocation, remainingItem);
                droppedItem.setPickupDelay(40);
            }
        }
    }
}
