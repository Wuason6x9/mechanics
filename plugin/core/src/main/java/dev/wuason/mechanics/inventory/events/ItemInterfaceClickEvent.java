package dev.wuason.mechanics.inventory.events;


import dev.wuason.mechanics.inventory.items.ItemInterface;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ItemInterfaceClickEvent extends InventoryClickEvent {

    private final ItemInterface itemInterface;
    private final InventoryClickEvent event;

    public ItemInterfaceClickEvent(ItemInterface itemInterface, InventoryClickEvent event) {
        super(event.getView(), event.getSlotType(), event.getSlot(), event.getClick(), event.getAction(), event.getHotbarButton());
        this.itemInterface = itemInterface;
        this.event = event;
    }

    public ItemInterface getItemInterface() {
        return itemInterface;
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCancelled(cancel);
    }
}
