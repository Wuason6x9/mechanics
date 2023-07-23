package dev.wuason.mechanics.compatibilities.plugins.oraxen;

import dev.wuason.mechanics.compatibilities.Implementation;
import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class OraxenImpl extends Implementation {
    public OraxenImpl() {
        super("or","Oraxen");
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if(!isEnabled()) return null;
        return OraxenItems.exists(id) ? OraxenItems.getItemById(id).build() : null;
    }

    @Override
    public String getAdapterID(ItemStack itemStack) {
        if(!isEnabled()) return null;
        if(OraxenItems.exists(itemStack)){
            return getType().toLowerCase(Locale.ENGLISH) + ":" + OraxenItems.getIdByItem(itemStack);
        }
        return null;
    }

    @Override
    public String getAdapterID(Block block) {
        if(!isEnabled()) return null;
        if(OraxenBlocks.isOraxenBlock(block)){
            return getType().toLowerCase(Locale.ENGLISH) + ":" + OraxenBlocks.getOraxenBlock(block.getLocation()).getItemID();
        }
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        if(!isEnabled()) return false;
        return OraxenItems.exists(id);
    }
}
