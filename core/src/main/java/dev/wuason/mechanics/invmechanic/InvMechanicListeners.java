package dev.wuason.mechanics.invmechanic;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.invmechanic.events.CloseEvent;
import dev.wuason.mechanics.invmechanic.types.InvCustom;
import dev.wuason.mechanics.invmechanic.types.InvCustomAnvil;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class InvMechanicListeners implements Listener {

    private final Mechanics core;

    public InvMechanicListeners(Mechanics core) {
        this.core = core;
    }

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof InvCustom) {
            InvCustom holder = (InvCustom) event.getInventory().getHolder();
            holder.handleClick(event);
        }
    }

    @EventHandler
    public void onInventoryOpen(org.bukkit.event.inventory.InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof InvCustom) {
            InvCustom holder = (InvCustom) event.getInventory().getHolder();
            holder.handleOpen(event);
        }
    }

    @EventHandler
    public void onInventoryClose(org.bukkit.event.inventory.InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof InvCustom) {
            InvCustom holder = (InvCustom) event.getInventory().getHolder();
            CloseEvent closeEvent = new CloseEvent(event);
            holder.handleClose(closeEvent);
            if (closeEvent.isCancelled()) {
                Bukkit.getScheduler().runTask(core, () -> {
                    if(holder instanceof InvCustomAnvil){
                        InvCustomAnvil invAnvil = (InvCustomAnvil) holder;
                        invAnvil.open();
                    }
                    else event.getPlayer().openInventory(event.getInventory());
                });
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(org.bukkit.event.inventory.InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof InvCustom) {
            InvCustom holder = (InvCustom) event.getInventory().getHolder();
            holder.handleDrag(event);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event){
        if(event.getEntityType().equals(EntityType.PLAYER) && ((Player) event.getEntity()).getOpenInventory() != null && ((Player) event.getEntity()).getOpenInventory().getTopInventory() != null && ((Player) event.getEntity()).getOpenInventory().getTopInventory().getHolder() instanceof InvCustom){
            InvCustom holder = (InvCustom) ((Player) event.getEntity()).getOpenInventory().getTopInventory().getHolder();
            if(holder.isDamageCancel()){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerPickup(org.bukkit.event.player.PlayerPickupItemEvent event){
        if(event.getPlayer().getOpenInventory() != null && event.getPlayer().getOpenInventory().getTopInventory() != null && event.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof InvCustom){
            InvCustom holder = (InvCustom) event.getPlayer().getOpenInventory().getTopInventory().getHolder();
            if(holder.isPickupCancel()){
                event.setCancelled(true);
            }
        }
    }

    public void closeAll(){
        Bukkit.getOnlinePlayers().stream().filter(player -> player.getOpenInventory().getTopInventory().getHolder() instanceof InvCustom).forEach(Player::closeInventory);
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event){
        if(event.getPlugin().equals(core)){
            closeAll();
        }
    }
}
