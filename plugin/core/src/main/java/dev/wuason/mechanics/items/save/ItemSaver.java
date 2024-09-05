package dev.wuason.mechanics.items.save;

import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemSaver {
    private final NamespacedKey key = ItemsSaverManager.generateKey();


    public NamespacedKey getKey() {
        return key;
    }

    public ItemStack[] load(Player player) {
        return player.getPersistentDataContainer().get(key, DataType.ITEM_STACK_ARRAY);
    }

    public void save(Player player, ItemStack... items) {
        player.getPersistentDataContainer().set(key, DataType.ITEM_STACK_ARRAY, items);
    }

    public void remove(Player player) {
        player.getPersistentDataContainer().remove(key);
    }

    public boolean hasSavedItems(Player player) {
        return player.getPersistentDataContainer().has(key, DataType.ITEM_STACK_ARRAY);
    }
}
