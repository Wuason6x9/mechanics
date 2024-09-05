package dev.wuason.old.types.pages.content.anvil.events;

import dev.wuason.old.types.events.CustomEvent;
import dev.wuason.mechanics.inventory.items.ItemInterface;
import org.bukkit.event.inventory.InventoryClickEvent;

public class NextPageEvent extends CustomEvent {
    private final int actualPage;
    private final int nextPage;
    private final InventoryClickEvent event;
    private final ItemInterface itemInterface;

    public NextPageEvent(int actualPage, int nextPage, InventoryClickEvent event, ItemInterface itemInterface) {
        super("NEXT_PAGE_EVENT");

        this.actualPage = actualPage;
        this.nextPage = nextPage;
        this.event = event;
        this.itemInterface = itemInterface;
    }

    public int getActualPage() {
        return actualPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }

    public ItemInterface getItemInterface() {
        return itemInterface;
    }
}
