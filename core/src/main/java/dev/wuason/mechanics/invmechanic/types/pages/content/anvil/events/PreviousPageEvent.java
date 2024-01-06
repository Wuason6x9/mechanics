package dev.wuason.mechanics.invmechanic.types.pages.content.anvil.events;

import dev.wuason.mechanics.invmechanic.events.CustomEvent;
import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PreviousPageEvent extends CustomEvent {

    private final int actualPage;

    private final int previousPage;

    private final InventoryClickEvent event;

    private final ItemInterface itemInterface;

    public PreviousPageEvent(int actualPage, int previousPage, InventoryClickEvent event, ItemInterface itemInterface) {
        super("PREVIOUS_PAGE_EVENT");

        this.actualPage = actualPage;
        this.previousPage = previousPage;
        this.event = event;
        this.itemInterface = itemInterface;
    }

    public int getActualPage() {
        return actualPage;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }

    public ItemInterface getItemInterface() {
        return itemInterface;
    }
}
