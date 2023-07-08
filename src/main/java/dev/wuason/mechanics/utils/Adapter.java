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
import java.util.Arrays;
import java.util.List;

public class Adapter {
    //AÑADIR METODO PARA COLOCAR BLOQUES
    public static ItemStack getItemStack(String itemID){

        String type = itemID.substring(0,itemID.indexOf(":"));
        ItemType itemType = ItemType.valueOf(type.toUpperCase());

        itemID = itemID.substring(itemID.indexOf(":") + 1);

        switch (itemType){
            case SM:
                Object obj;
                Object obj2;
                Object obj3;
                Mechanic storageMechanic = Mechanics.getInstance().getMechanicsManager().getMechanic("storagemechanic");
                try {
                    obj = storageMechanic.getManagersClass().getClass().getMethod("getCustomBlockManager").invoke(storageMechanic.getManagersClass());
                    obj2 = obj.getClass().getMethod("getCustomBlockById",java.lang.String.class).invoke(obj,itemID);
                    if(obj2 != null){
                        obj3 = obj2.getClass().getMethod("getItemStack").invoke(obj2);
                        ItemStack itemStack = (ItemStack) obj3;
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setDisplayName(" ");
                        itemStack.setItemMeta(itemMeta);
                        return itemStack;
                    }
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                } catch (NoSuchMethodException e) {
                }
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

    public static ItemStack getItemStackOriginal(String itemID){

        String type = itemID.substring(0,itemID.indexOf(":"));
        ItemType itemType = ItemType.valueOf(type.toUpperCase());

        itemID = itemID.substring(itemID.indexOf(":") + 1);

        switch (itemType){
            case SM:
                Object obj;
                Object obj2;
                Object obj3;
                Mechanic storageMechanic = Mechanics.getInstance().getMechanicsManager().getMechanic("storagemechanic");
                try {
                    obj = storageMechanic.getManagersClass().getClass().getMethod("getCustomBlockManager").invoke(storageMechanic.getManagersClass());
                    obj2 = obj.getClass().getMethod("getCustomBlockById",java.lang.String.class).invoke(obj,itemID);
                    if(obj2 != null){
                        obj3 = obj2.getClass().getMethod("getItemStack").invoke(obj2);
                        return (ItemStack) obj3;
                    }
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                } catch (NoSuchMethodException e) {
                }
            case MC:
                Material material = null;
                try {
                    material = Material.valueOf(itemID.toUpperCase());
                }catch (IllegalArgumentException argumentException){
                    return null;
                }
                return new ItemStack(material);
            case IA:
                return CustomStack.getInstance(itemID).getItemStack();
            case OR:
                return OraxenItems.getItemById(itemID).build();
        }

        return null;

    }

    public static List<ItemStack> getItemsStackOriginal(List<String> i){
        List<ItemStack> itemStacks = new ArrayList<>();
        for(String str : i){
            itemStacks.add(getItemStackOriginal(str));
        }
        return itemStacks;
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
        Mechanic storageMechanic = Mechanics.getInstance().getMechanicsManager().getMechanic("storagemechanic");
        if(storageMechanic != null){
            Object obj;
            Object obj2;
            try {
                obj = storageMechanic.getManagersClass().getClass().getMethod("getCustomBlockManager").invoke(storageMechanic.getManagersClass());
                obj2 = obj.getClass().getMethod("getCustomBlockIdFromItemStack",org.bukkit.inventory.ItemStack.class).invoke(obj,itemStack);
                if(obj2 != null){
                    id = "sm:" + ((String) obj2);
                }
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }
        return id;
    }

    public static String getAdapterID(Block block){
        String id = "mc:" + block.getType().toString().toLowerCase();
        if(isOraxenEnabled()){
            if(OraxenBlocks.isOraxenBlock(block)){
                id = "or:" + OraxenBlocks.getOraxenBlock(block.getLocation()).getItemID();
            }
        }
        if(isItemsAdderEnabled()){
            if(CustomBlock.byAlreadyPlaced(block) != null){
                id = "ia:" + CustomBlock.byAlreadyPlaced(block).getNamespacedID();
            }
        }
        Mechanic storageMechanic = Mechanics.getInstance().getMechanicsManager().getMechanic("storagemechanic");
        if(storageMechanic != null){
            Object obj;
            Object obj2;
            try {
                obj = storageMechanic.getManagersClass().getClass().getMethod("getCustomBlockManager").invoke(storageMechanic.getManagersClass());
                obj2 = obj.getClass().getMethod("getCustomBlockIdFromBlock",org.bukkit.block.Block.class).invoke(obj,block);
                if(obj2 != null){
                    id = "sm:" + ((String) obj2);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return id;
    }

    public enum ItemType{
        IA,
        OR,
        MC,
        SM,
        MMOITEMS,
        MYTHICMOBS,
        CB

    }

    public static boolean isItemsAdderEnabled(){
        return Bukkit.getPluginManager().isPluginEnabled("ItemsAdder");
    }
    public static boolean isOraxenEnabled(){
        return Bukkit.getPluginManager().isPluginEnabled("Oraxen");
    }

}