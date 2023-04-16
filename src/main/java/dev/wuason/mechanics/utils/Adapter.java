package dev.wuason.mechanics.utils;

import dev.lone.itemsadder.api.CustomStack;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Adapter {
    public static ItemStack getItemStack(String itemID){

        String type = itemID.substring(0,itemID.indexOf(":"));
        ItemType itemType = ItemType.valueOf(type.toUpperCase());

        itemID = itemID.substring(itemID.indexOf(":") + 1);

        switch (itemType){
            case MC:
                ItemStack itemStack = new ItemStack(Material.valueOf(itemID.toUpperCase()));
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(" ");
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            case IA:
                ItemStack itemStackItemsAdder = CustomStack.getInstance(itemID).getItemStack();
                ItemMeta itemMetaItemsAdder = itemStackItemsAdder.getItemMeta();
                itemMetaItemsAdder.setDisplayName(" ");
                itemStackItemsAdder.setItemMeta(itemMetaItemsAdder);
                return itemStackItemsAdder;
            case OR:
                ItemStack itemStackOraxen = OraxenItems.getItemById(itemID).build();
                ItemMeta itemMetaOraxen = itemStackOraxen.getItemMeta();
                itemMetaOraxen.setDisplayName(" ");
                itemStackOraxen.setItemMeta(itemMetaOraxen);
                return itemStackOraxen;
        }

        return null;

    }

    public enum ItemType{
        IA,
        OR,
        MC
    }
}