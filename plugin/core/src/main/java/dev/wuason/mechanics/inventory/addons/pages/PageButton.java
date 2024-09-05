package dev.wuason.mechanics.inventory.addons.pages;

import dev.wuason.mechanics.inventory.InvCustom;
import dev.wuason.mechanics.inventory.items.ItemInterface;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PageButton extends ItemInterface {

    public PageButton(int slot, ItemStack itemStack, String name) {
        super(slot, itemStack, name);
    }

    public PageButton(int slot, ItemStack itemStack) {
        super(slot, itemStack);
    }

    public PageButton(int slot, String id, ItemStack itemStack) {
        super(slot, id, itemStack);
    }

    public PageButton(int slot, ItemStack itemStack, String id, String name) {
        super(slot, itemStack, id, name);
    }
}
