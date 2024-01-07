package dev.wuason.mechanics.invmechanic;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.invmechanic.events.CloseEvent;
import dev.wuason.mechanics.invmechanic.types.InvCustom;
import dev.wuason.mechanics.invmechanic.types.InvCustomAnvil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;

public class InvMechanicListeners implements Listener {

    private final Mechanics core;

    public InvMechanicListeners(Mechanics core) {
        this.core = core;
    }


    /**
     * Handles click events in custom inventories. When a player clicks inside an inventory,
     * this method checks if the inventory is a custom type (InvCustom). If so, it delegates
     * the click handling to the custom inventory's specific click handler.
     *
     * @param event The InventoryClickEvent triggered when an inventory is clicked.
     */
    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof InvCustom holder) {
            holder.handleClick(event);
        }
    }

    /**
     * Handles the opening of custom inventories. When a player opens an inventory,
     * this method verifies if it's a custom inventory (InvCustom). If it is, it calls
     * the custom inventory's open event handler to manage the opening process.
     *
     * @param event The InventoryOpenEvent triggered when an inventory is opened.
     */
    @EventHandler
    public void onInventoryOpen(org.bukkit.event.inventory.InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof InvCustom holder) {
            holder.handleOpen(event);
        }
    }

    /**
     * Manages the closing of custom inventories. This method checks if the closing
     * inventory is of the custom type (InvCustom). If so, it processes the close event
     * through the custom inventory's close event handler. Additionally, if the close
     * event is cancelled for an anvil inventory, it reopens the inventory for the player.
     *
     * @param event The InventoryCloseEvent triggered when an inventory is closed.
     */
    @EventHandler
    public void onInventoryClose(org.bukkit.event.inventory.InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof InvCustom holder) {
            CloseEvent closeEvent = new CloseEvent(event);
            holder.handleClose(closeEvent);
            if (closeEvent.isCancelled()) {
                Bukkit.getScheduler().runTask(core, () -> {
                    if (holder instanceof InvCustomAnvil invAnvil) {
                        invAnvil.open();
                    } else event.getPlayer().openInventory(event.getInventory());
                });
            }
        }
    }

    /**
     * Handles item drag events within custom inventories. When items are dragged in an inventory,
     * this method checks if it's a custom inventory (InvCustom). If so, the drag event is processed
     * by the custom inventory's drag event handler.
     *
     * @param event The InventoryDragEvent triggered when items are dragged within an inventory.
     */
    @EventHandler
    public void onInventoryDrag(org.bukkit.event.inventory.InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof InvCustom holder) {
            holder.handleDrag(event);
        }
    }

    /**
     * Manages damage events to players who have a custom inventory open. If a player
     * is damaged, and they have a custom inventory (InvCustom) open, this method checks
     * if the inventory has damage cancellation enabled. If yes, the damage event is cancelled.
     *
     * @param event The EntityDamageEvent triggered when an entity takes damage.
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            Inventory topInv = player.getOpenInventory().getTopInventory();
            if (topInv.getHolder() instanceof InvCustom holder) {
                if (holder.isDamageCancel()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Handles the event when a player picks up an item while having a custom inventory open.
     * This method checks if the top inventory open for the player is a custom inventory that
     * disallows item pickups. If so, the item pickup is cancelled.
     *
     * @param event The PlayerPickupItemEvent triggered when a player picks up an item.
     */
    @EventHandler
    public void onPlayerPickup(org.bukkit.event.player.PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Inventory topInventory = player.getOpenInventory().getTopInventory();

        // Check if the top inventory is a custom inventory and if it cancels pickups
        if (topInventory.getHolder() instanceof InvCustom customInventory) {
            if (customInventory.isPickupCancel()) {
                event.setCancelled(true);
            }
        }
    }


    /**
     * Handles the item pickup event for all entities. This method is invoked by the EntityPickupItemEvent,
     * which can be triggered by any entity type, including non-player entities. Similar to the player-specific
     * event handler, it checks if the entity is a player and if the player's top inventory is a custom inventory
     * that prohibits item pickups. If these conditions are met, the item pickup is cancelled.
     *
     * @param event The EntityPickupItemEvent triggered when any entity picks up an item.
     */
    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            Inventory topInventory = player.getOpenInventory().getTopInventory();
            if (topInventory.getHolder() instanceof InvCustom customInventory) {
                if (customInventory.isPickupCancel()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Closes all open custom inventories for online players. This method is typically called
     * when the plugin is disabled to ensure all custom inventories are properly closed.
     */
    public void closeAll() {
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getOpenInventory().getTopInventory().getHolder() instanceof InvCustom).forEach(Player::closeInventory);
    }

    /**
     * Handles the disabling of the plugin. When the plugin is disabled, this method ensures
     * that all open custom inventories for online players are closed.
     *
     * @param event The PluginDisableEvent triggered when a plugin is disabled.
     */
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(core)) {
            closeAll();
        }
    }
}
