package dev.wuason.mechanics.invmechanic.types.pages.content.normal.events;
import dev.wuason.mechanics.invmechanic.events.CustomEvent;
import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import dev.wuason.mechanics.invmechanic.types.pages.content.normal.InvCustomPagesContent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PreviousPageEvent extends CustomEvent {



    private final InventoryClickEvent event;
    private final ItemInterface itemInterface;

    private final int actualPage;
    private final int backPage;
    private final InvCustomPagesContent page;

    public PreviousPageEvent(InvCustomPagesContent page, InventoryClickEvent event, int actualPage, int backPage, ItemInterface itemInterface) {

        super("PREVIOUS_PAGE_EVENT");
        this.page = page;
        this.actualPage = actualPage;
        this.backPage = backPage;
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

    public int getBackPage() {
        return backPage;
    }
}
