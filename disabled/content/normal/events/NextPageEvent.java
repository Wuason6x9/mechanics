package dev.wuason.old.types.pages.content.normal.events;
import dev.wuason.old.types.events.CustomEvent;
import dev.wuason.mechanics.inventory.items.ItemInterface;
import dev.wuason.old.types.pages.content.normal.InvCustomPagesContent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class NextPageEvent extends CustomEvent {



    private final InventoryClickEvent event;
    private final ItemInterface itemInterface;

    private final int actualPage;
    private final int nextPage;
    private final InvCustomPagesContent page;

    public NextPageEvent(InvCustomPagesContent page, InventoryClickEvent event, int actualPage, int nextPage, ItemInterface itemInterface) {

        super("NEXT_PAGE_EVENT");
        this.page = page;
        this.actualPage = actualPage;
        this.nextPage = nextPage;
        this.event = event;
        this.itemInterface = itemInterface;

    }

    //Getters

    public InvCustomPagesContent getPage() {
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

    public int getNextPage() {
        return nextPage;
    }
}
