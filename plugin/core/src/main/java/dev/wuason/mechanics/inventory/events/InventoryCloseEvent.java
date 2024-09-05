package dev.wuason.mechanics.inventory.events;

import org.bukkit.event.Cancellable;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class InventoryCloseEvent extends org.bukkit.event.inventory.InventoryCloseEvent implements Cancellable {
    private boolean cancelled = false;
    public InventoryCloseEvent(@NotNull InventoryView transaction, @NotNull Reason reason) {
        super(transaction, reason);
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
