package dev.wuason.mechanics.inventory.addons.pages;

import dev.wuason.mechanics.inventory.InvCustom;
import dev.wuason.mechanics.inventory.items.ItemInterface;
import dev.wuason.mechanics.inventory.types.anvil.multipage.InvCustomAnvilPages;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

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
