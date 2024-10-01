package dev.wuason.mechanics.inventory.items.def.pages;

import dev.wuason.mechanics.inventory.InvCustom;
import dev.wuason.mechanics.inventory.items.ItemInterface;
import dev.wuason.mechanics.inventory.pages.MultiPages;
import dev.wuason.mechanics.inventory.pages.Page;
import dev.wuason.mechanics.inventory.pages.PageContent;
import dev.wuason.mechanics.inventory.pages.Pageable;
import dev.wuason.mechanics.inventory.types.anvil.pages.InvCustomAnvilPages;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ContentItem<T> extends ItemInterface {

    private final T data;
    private final int page;

    public ContentItem(int slot, ItemStack itemStack, T data, int page) {
        super(slot, itemStack);
        this.data = data;
        this.page = page;
    }


    @Override
    public void onClick(InventoryClickEvent event, InvCustom invCustom) {
        if (invCustom instanceof MultiPages pages) {
            for (Page page : pages.getPages()) {
                if (page.getAreaSlots().contains(event.getRawSlot())) page.handleClickContent(event, this);
                return;
            }
        }
        if (invCustom instanceof InvCustomAnvilPages pageable) {
            pageable.handleClickContent(event, this);
        }
    }

    public T getData() {
        return data;
    }

    public int getPage() {
        return page;
    }
}
