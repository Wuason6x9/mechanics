package dev.wuason.mechanics.utils;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.mechanics.Mechanic;
import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Adapter {
    public static ItemStack getItemStack(String itemID){

        String type = itemID.substring(0,itemID.indexOf(":"));
        ItemType itemType = ItemType.valueOf(type.toUpperCase());

        itemID = itemID.substring(itemID.indexOf(":") + 1);

        switch (itemType){
            case SM:

            case MC:
                Material material = null;
                try {
                    material = Material.valueOf(itemID.toUpperCase());
                }catch (IllegalArgumentException argumentException){
                    return null;
                }
                ItemStack itemStack = new ItemStack(material);
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

    public static String getAdapterID(ItemStack itemStack){
        String id = "mc:" + itemStack.getType().toString().toLowerCase();
        if(isOraxenEnabled()){
            if(OraxenItems.exists(itemStack)){
                id = "or:" + OraxenItems.getIdByItem(itemStack);
            }
        }
        if(isItemsAdderEnabled()){
            if(CustomStack.byItemStack(itemStack) != null){
                id = "ia:" + CustomStack.byItemStack(itemStack).getNamespacedID();
            }
        }
        Mechanic storageMechanic = Mechanics.getInstance().getMechanicsManager().getMechanic("StorageMechanic");
        if(storageMechanic != null){

        }
        return id;
    }

    public static String getAdapterID(Block block){
        String id = "mc:" + block.getType().toString().toLowerCase();
        if(isOraxenEnabled()){
            if(OraxenBlocks.isOraxenBlock(block)){
                id = "or:" + OraxenBlocks.getBlockMechanic(block).getItemID();
            }
        }
        if(isItemsAdderEnabled()){
            if(CustomBlock.byAlreadyPlaced(block) != null){
                id = "ia:" + CustomBlock.byAlreadyPlaced(block).getNamespacedID();
            }
        }
        Mechanic storageMechanic = Mechanics.getInstance().getMechanicsManager().getMechanic("StorageMechanic");
        if(storageMechanic != null){

        }
        return id;
    }

    public enum ItemType{
        IA,
        OR,
        MC,
        SM
    }

    public static boolean isItemsAdderEnabled(){
        return Bukkit.getPluginManager().isPluginEnabled("ItemsAdder");
    }
    public static boolean isOraxenEnabled(){
        return Bukkit.getPluginManager().isPluginEnabled("Oraxen");
    }

}