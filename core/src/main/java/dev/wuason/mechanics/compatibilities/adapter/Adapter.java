package dev.wuason.mechanics.compatibilities.adapter;

import de.tr7zw.changeme.nbtapi.NBT;
import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.compatibilities.adapter.plugins.customitems.CustomItemsImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.executableblocks.ExecutableBlocksImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.executableitems.ExecutableItemsImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.itemsadder.ItemsAdderImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.mmoitems.MMOItemsImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.mythic.MythicImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.mythiccucible.MythicCrucibleImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.oraxen.OraxenImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.storagemechanic.StorageMechanicImpl;
import dev.wuason.mechanics.utils.AdventureUtils;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Adapter {

    private Mechanics core;
    private static Adapter instance;

    private static HashMap<String, Implementation> types = new HashMap<>(){{

        put("MC",null);

    }};

    public Adapter(Mechanics core){
        this.core = core;
        instance = this;
        load();
    }
    public void load(){
        Implementation[] implementations = {new VanillaImpl(), new StorageMechanicImpl(), new OraxenImpl(), new MythicImpl(), new MMOItemsImpl(), new ItemsAdderImpl(), new ExecutableItemsImpl(), new ExecutableBlocksImpl(), new CustomItemsImpl(), new MythicCrucibleImpl()};
        AdventureUtils.sendMessagePluginConsole("<gold>Implementations: <aqua>" + types.keySet().stream().toList());
    }

    public ItemStack getItemStack(String itemID){
        String type = itemID.substring(0,itemID.indexOf(":"));
        itemID = itemID.substring(itemID.indexOf(":") + 1);
        Implementation impl = types.get(type.toUpperCase(Locale.ENGLISH));
        if(impl == null) return null;
        return impl.getAdapterItem(itemID);
    }
    public String computeAdapterId(String itemId){
        String type = itemId.substring(0,itemId.indexOf(":")).toUpperCase(Locale.ENGLISH);
        if(type.equals("MC")){
            return getAdapterID(getItemStack(itemId));
        }
        return itemId;
    }

    public String getAdapterID(ItemStack i){
        ItemStack itemStack = i.clone();
        if(itemStack == null) return null;
        itemStack.setAmount(1);
        String id = "mc:" + NBT.itemStackToNBT(itemStack);
        for(Implementation impl : types.values()){
            String itemId = impl.getAdapterID(itemStack);
            if(itemId != null) id = itemId;
        }
        return id;
    }

    public String getAdapterID(Block block){
        if(block == null) return null;
        String id = "mc:" + NBT.itemStackToNBT(new ItemStack(block.getType()));
        for(Implementation impl : types.values()){
            String itemId = impl.getAdapterID(block);
            if(itemId != null) id = itemId;
        }
        return id;
    }
    public boolean existAdapterID(String itemID){
        String type = itemID.substring(0,itemID.indexOf(":"));
        itemID = itemID.substring(itemID.indexOf(":") + 1);
        for(Implementation impl : types.values()){
            if(impl.existItemAdapter(itemID)) return true;
        }
        return false;
    }

    public List<ItemStack> getItemsStack(List<String> i){
        List<ItemStack> itemStacks = new ArrayList<>();
        for(String str : i){
            itemStacks.add(getItemStack(str));
        }
        return itemStacks;
    }

    public boolean isItemsValid(List<String> i){
        for(String str : i){
            if(!existAdapterID(str)) return false;
        }
        return true;
    }

    public static HashMap<String, Implementation> getTypes() {
        return types;
    }

    public static Adapter getInstance() {
        return instance;
    }
}