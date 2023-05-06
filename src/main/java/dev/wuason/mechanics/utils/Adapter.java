package dev.wuason.mechanics.utils;

import dev.lone.itemsadder.api.CustomStack;
import dev.wuason.mechanics.Mechanics;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Adapter {
    public static ItemStack getItemStack(String itemID){

        String type = itemID.substring(0,itemID.indexOf(":"));
        ItemType itemType = ItemType.valueOf(type.toUpperCase());

        itemID = itemID.substring(itemID.indexOf(":") + 1);

        switch (itemType){
            case SM:
                Object obj = null;
                ItemStack itemStackStorageMechanic = null;
                try {
                    obj = Mechanics.getInstance().getMechanicsManager().getMechanic("StorageMechanic").getManagersClass().getMethod("getCustomBlockManager").getDefaultValue().getClass().getMethod("getCustomBlockById").invoke(itemID);
                } catch (NoSuchMethodException e) {
                } catch (InvocationTargetException e) {
                } catch (IllegalAccessException e) {
                }
                if(obj != null){
                    try {
                        itemStackStorageMechanic = (ItemStack) obj.getClass().getMethod("getItemStack").getDefaultValue();
                    } catch (NoSuchMethodException e) {
                    }
                }
                return itemStackStorageMechanic;
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

    public static List<ItemStack> getItemsStack(List<String> i){
        List<ItemStack> itemStacks = new ArrayList<>();
        for(String str : i){
            itemStacks.add(getItemStack(str));
        }
        return itemStacks;
    }

    public static boolean isItemsValid(List<String> i){
        for(String str : i){
            if(!isItemValid(str)) return false;
        }
        return true;
    }

    public static boolean isItemValid(String item){
        if(getItemStack(item) != null) return true;
        return false;
    }

    public enum ItemType{
        IA,
        OR,
        MC,
        SM
    }
}