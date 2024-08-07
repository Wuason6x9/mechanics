package dev.wuason.mechanics.invmechanic.events;


import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ItemInterfaceClickEvent extends CustomEvent {

    private ItemInterface itemInterface;
    private InventoryClickEvent event;


    public ItemInterfaceClickEvent(ItemInterface itemInterface, InventoryClickEvent event) {
        super("ITEM_INTERFACE_CLICK_EVENT");
        this.itemInterface = itemInterface;
        this.event = event;
    }

    public ItemInterface getItemInterface() {
        return itemInterface;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }
}
