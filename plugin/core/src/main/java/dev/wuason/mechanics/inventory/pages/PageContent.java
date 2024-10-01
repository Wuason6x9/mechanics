package dev.wuason.mechanics.inventory.pages;

import dev.wuason.mechanics.inventory.items.def.pages.ContentItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface PageContent<T> extends Pageable {

    List<T> getContent();

    void setContent(List<T> content);

    void addContent(T content);

    void removeContent(T content);

    void clearContent();

    void addAllContent(List<T> content);

    void handleClickContent(InventoryClickEvent event, ContentItem<T> contentItem);

    ItemStack handleContentPage(int slot, int page, T content);

    void handleUpdate();
}
