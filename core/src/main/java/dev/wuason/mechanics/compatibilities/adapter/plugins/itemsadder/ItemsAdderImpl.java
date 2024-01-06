package dev.wuason.mechanics.compatibilities.adapter.plugins.itemsadder;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import dev.wuason.mechanics.compatibilities.adapter.Implementation;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class ItemsAdderImpl extends Implementation {
    public ItemsAdderImpl() {
        super("ia","ItemsAdder");
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if(!isEnabled()) return null;
        CustomStack customStack = CustomStack.getInstance(id);
        return customStack != null ? customStack.getItemStack() : null;
    }

    @Override
    public String getAdapterID(ItemStack itemStack) {
        if(!isEnabled()) return null;
        if(CustomStack.byItemStack(itemStack) != null){
            return getType().toLowerCase(Locale.ENGLISH) + ":" + CustomStack.byItemStack(itemStack).getNamespacedID();
        }
        return null;
    }

    @Override
    public String getAdapterID(Block block) {
        if(!isEnabled()) return null;
        if(CustomBlock.byAlreadyPlaced(block) != null){
            return getType().toLowerCase(Locale.ENGLISH) + ":" + CustomBlock.byAlreadyPlaced(block).getNamespacedID();
        }
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        return getAdapterItem(id) != null;
    }
}
