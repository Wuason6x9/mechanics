package dev.wuason.mechanics.compatibilities.adapter.plugins.oraxen;

import de.tr7zw.changeme.nbtapi.NBT;
import dev.wuason.mechanics.compatibilities.adapter.ImplementationAdapter;
import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class OraxenImpl extends ImplementationAdapter {
    public OraxenImpl() {
        super("or","Oraxen");
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if(!isEnabled()) return null;
        return OraxenItems.exists(id) ? OraxenItems.getItemById(id).build() : null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if(!isEnabled()) return null;
        if(OraxenItems.exists(itemStack)){
            return getType().toLowerCase(Locale.ENGLISH) + ":" + OraxenItems.getIdByItem(itemStack);
        }
        return null;
    }

    @Override
    public String getAdapterId(Block block) {
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

    @Override
    public String computeAdapterId(String itemId) {
        if(!isEnabled()) return null;
        ItemStack item = getAdapterItem(itemId);
        if(item == null) return null;
        return NBT.itemStackToNBT(item).toString();
    }
}
