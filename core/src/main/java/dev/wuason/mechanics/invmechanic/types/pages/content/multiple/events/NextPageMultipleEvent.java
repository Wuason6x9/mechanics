package dev.wuason.mechanics.invmechanic.types.pages.content.multiple.events;
import dev.wuason.mechanics.invmechanic.events.CustomEvent;
import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import dev.wuason.mechanics.invmechanic.types.pages.content.multiple.PageCustomInfo;
import org.bukkit.event.inventory.InventoryClickEvent;

public class NextPageMultipleEvent extends CustomEvent {

    private final PageCustomInfo<?> page;

    private final InventoryClickEvent event;
    private final ItemInterface itemInterface;

    private final int actualPage;
    private final int nextPage;

    public NextPageMultipleEvent(PageCustomInfo<?> page, InventoryClickEvent event, int actualPage, int nextPage, ItemInterface itemInterface) {

        super("NEXT_PAGE_EVENT");
        this.page = page;
        this.actualPage = actualPage;
        this.nextPage = nextPage;
        this.event = event;
        this.itemInterface = itemInterface;

    }

    //Getters

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

    public int getNextPage() {
        return nextPage;
    }
}
