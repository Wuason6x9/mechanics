package dev.wuason.old.types.pages.content.normal.events;

import dev.wuason.old.types.events.CustomEvent;
import dev.wuason.old.types.pages.content.normal.InvCustomPagesContent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ContentClickEvent extends CustomEvent {
    private final InvCustomPagesContent inventoryCustomPagesContent;
    private final InventoryClickEvent event;
    private final Object content;

    public ContentClickEvent(InvCustomPagesContent inventoryCustomPagesContent, InventoryClickEvent event, Object content) {
        super("CONTENT_CLICK_EVENT");
        this.inventoryCustomPagesContent = inventoryCustomPagesContent;
        this.event = event;
        this.content = content;
    }

    public InvCustomPagesContent getInventoryCustomPagesContent() {
        return inventoryCustomPagesContent;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }

    public Object getContent() {
        return content;
    }

}
