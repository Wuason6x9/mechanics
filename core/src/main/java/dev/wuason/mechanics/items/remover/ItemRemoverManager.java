package dev.wuason.mechanics.items.remover;

import dev.wuason.mechanics.Mechanics;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ItemRemoverManager implements Listener {

    private final Mechanics core;
    private static final List<Function<ItemStack, Boolean>> checks = new ArrayList<>();

    public ItemRemoverManager(Mechanics core) {
        this.core = core;
    }

    public static void addCheck(Function<ItemStack, Boolean> check) {
        checks.add(check);
    }

    public static void removeCheck(Function<ItemStack, Boolean> check) {
        checks.remove(check);
    }

    public static void clearChecks() {
        checks.clear();
    }

    public static List<Function<ItemStack, Boolean>> getChecks() {
        return checks;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void itemEvent(PlayerDropItemEvent event) {

        if (event.isCancelled()) return;

        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();

        if(itemStack == null || itemStack.getType().isAir()) return;

        for (Function<ItemStack, Boolean> check : checks) {
            if (check.apply(itemStack)) {
                event.setCancelled(true);
                item.remove();
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void itemEvent(EntityPickupItemEvent event) {

        if (event.isCancelled()) return;

        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();

        if(itemStack == null || itemStack.getType().isAir()) return;

        for (Function<ItemStack, Boolean> check : checks) {
            if (check.apply(itemStack)) {
                event.setCancelled(true);
                item.remove();
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void itemEvent(BlockPlaceEvent event) {

        if (event.isCancelled()) return;

        try {
            ItemStack itemStack = event.getItemInHand();

            if(itemStack == null || itemStack.getType().isAir()) return;

            for (Function<ItemStack, Boolean> check : checks) {
                if (check.apply(itemStack)) {
                    event.setCancelled(true);
                    event.getPlayer().getInventory().setItem(event.getHand(), new ItemStack(Material.AIR));
                    return;
                }
            }

        } catch (Exception a) {}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void itemEvent(InventoryClickEvent event) {
        if (event.isCancelled()) return;

        if(event.getClickedInventory() == null || !event.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;

        try {
            ItemStack itemStack = event.getCurrentItem();

            if(itemStack == null || itemStack.getType().isAir()) return;

            for (Function<ItemStack, Boolean> check : checks) {
                if (check.apply(itemStack)) {
                    event.setCancelled(true);
                    event.getWhoClicked().getInventory().remove(itemStack);
                    return;
                }
            }

        } catch (Exception a) {}
    }
}
