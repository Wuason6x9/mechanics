package dev.wuason.mechanics.invmechanic.events;

import org.bukkit.event.inventory.InventoryCloseEvent;

public class CloseEvent extends CustomEvent {
    private boolean cancelled = false;
    private final InventoryCloseEvent event;

    public CloseEvent(InventoryCloseEvent event) {
        super("CLOSE_EVENT");
        this.event = event;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public InventoryCloseEvent getEvent() {
        return event;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }


}
