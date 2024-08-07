package dev.wuason.mechanics.invmechanic.types.pages.content.multiple.events;


import dev.wuason.mechanics.invmechanic.events.CustomEvent;
import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import dev.wuason.mechanics.invmechanic.types.pages.content.multiple.PageCustomInfo;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PreviousPageMultipleEvent extends CustomEvent {
    private final PageCustomInfo<?> page;
    private final int actualPage;
    private final int previousPage;

    private final InventoryClickEvent event;
    private final ItemInterface itemInterface;

    public PreviousPageMultipleEvent(PageCustomInfo<?> page, InventoryClickEvent event, int actualPage, int previousPage, ItemInterface itemInterface) {
        super("PREVIOUS_PAGE_EVENT");
        this.page = page;
        this.event = event;
        this.actualPage = actualPage;
        this.previousPage = previousPage;
        this.itemInterface = itemInterface;
    }

    public PageCustomInfo<?> getPage() {
        return page;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }

    public ItemInterface getItemInterface() {
        return itemInterface;
    }

    public int getActualPage() {
        return actualPage;
    }

    public int getPreviousPage() {
        return previousPage;
    }

}
