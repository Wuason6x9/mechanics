package dev.wuason.mechanics.inventory.addons.pages;

import dev.wuason.mechanics.inventory.InvCustom;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public final class BackPageButton extends PageButton {


    public BackPageButton(int slot, ItemStack itemStack, String name) {
        super(slot, itemStack, name);
    }

    public BackPageButton(int slot, ItemStack itemStack) {
        super(slot, itemStack);
    }

    public BackPageButton(int slot, String id, ItemStack itemStack) {
        super(slot, id, itemStack);
    }

    public BackPageButton(int slot, ItemStack itemStack, String id, String name) {
        super(slot, itemStack, id, name);
    }

    @Override
    public void onClick(InventoryClickEvent event, InvCustom invCustom) {
        if (invCustom instanceof Pageable) {
            ((Pageable) invCustom).handlePreviousPage(this);
        }
    }
}
