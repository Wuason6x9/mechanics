package dev.wuason.old.types.pages.content.normal;

import dev.wuason.mechanics.inventory.InvCustom;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.function.Function;

public class InvCustomPagesContent extends InvCustom {
    private final InvCustomPagesContentManager manager;
    private int page = -1;
    public InvCustomPagesContent(String title, int size, InvCustomPagesContentManager manager, int page) {
        super(title, size);
        this.manager = manager;
        this.page = page;
    }

    public InvCustomPagesContent(String title, InventoryType type, InvCustomPagesContentManager manager, int page) {
        super(title, type);
        this.manager = manager;
        this.page = page;
    }

    public InvCustomPagesContent(String title, int size, Function<InvCustom, Inventory> function, InvCustomPagesContentManager manager, int page) {
        super(title, size, function);
        this.manager = manager;
        this.page = page;
    }

    public InvCustomPagesContent(Function<InvCustom, Inventory> function, InvCustomPagesContentManager manager, int page) {
        super(function);
        this.manager = manager;
        this.page = page;
    }

    public InvCustomPagesContentManager getManager() {
        return manager;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
