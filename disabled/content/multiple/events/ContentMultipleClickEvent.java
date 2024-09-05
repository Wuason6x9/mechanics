package dev.wuason.old.types.pages.content.multiple.events;

import dev.wuason.old.types.events.CustomEvent;
import dev.wuason.old.types.pages.content.multiple.PageCustomInfo;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ContentMultipleClickEvent extends CustomEvent {

    private int slot;
    private int actualPage;
    private PageCustomInfo<?> pageCustomInfo;
    private Object content;
    private InventoryClickEvent event;

    public ContentMultipleClickEvent(int slot, int actualPage, PageCustomInfo<?> pageCustomInfo, Object content, InventoryClickEvent event) {
        super("CONTENT_CLICK_EVENT");
        this.slot = slot;
        this.actualPage = actualPage;
        this.pageCustomInfo = pageCustomInfo;
        this.content = content;
        this.event = event;
    }

    //******************** GETTERS ********************

    public int getSlot() {
        return slot;
    }

    public int getActualPage() {
        return actualPage;
    }

    public PageCustomInfo<?> getPageCustomInfo() {
        return pageCustomInfo;
    }

    public Object getContent() {
        return content;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }

}
