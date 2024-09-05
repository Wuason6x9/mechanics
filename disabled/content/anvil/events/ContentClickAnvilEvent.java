package dev.wuason.old.types.pages.content.anvil.events;

import dev.wuason.old.types.events.CustomEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ContentClickAnvilEvent<T> extends CustomEvent {

    private final InventoryClickEvent event;
    private final T content;
    private final int slot;
    private final int actualPage;

    public ContentClickAnvilEvent(InventoryClickEvent event, T content, int slot, int actualPage) {
        super("CONTENT_CLICK_ANVIL_EVENT");
        this.event = event;
        this.content = content;
        this.slot = slot;
        this.actualPage = actualPage;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }

    public T getContent() {
        return content;
    }

    public int getSlot() {
        return slot;
    }

    public int getActualPage() {
        return actualPage;
    }
}
