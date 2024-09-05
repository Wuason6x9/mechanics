package dev.wuason.mechanics.items.save;

import com.jeff_media.morepersistentdatatypes.DataType;
import dev.wuason.mechanics.utils.StorageUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.UUID;

public class ItemsSaverManager implements Listener {

    public static final String ITEMS_SAVED_NAMESPACED = "items_saved_mechanics";

    public static NamespacedKey generateKey() {
        return new NamespacedKey(ITEMS_SAVED_NAMESPACED, UUID.randomUUID().toString());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        PersistentDataContainer pdc = event.getPlayer().getPersistentDataContainer();
        pdc.getKeys().stream().filter(namespacedKey -> namespacedKey.getNamespace().equals(ITEMS_SAVED_NAMESPACED)).forEach(namespacedKey -> {
            handle(event.getPlayer(), pdc, namespacedKey);
        });
    }

    public void handle(Player player, PersistentDataContainer pdc, NamespacedKey namespacedKey) {
        ItemStack[] items = pdc.get(namespacedKey, DataType.ITEM_STACK_ARRAY);
        pdc.remove(namespacedKey);
        player.getInventory().clear();
        player.getInventory().setContents(items);
    }
}
