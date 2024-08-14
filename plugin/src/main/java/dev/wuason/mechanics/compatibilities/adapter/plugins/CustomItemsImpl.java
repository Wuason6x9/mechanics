package dev.wuason.mechanics.compatibilities.adapter.plugins;
import de.tr7zw.changeme.nbtapi.NBT;

import dev.wuason.customItemsAPI.CustomItemsAPI;
import dev.wuason.mechanics.compatibilities.adapter.ImplementationAdapter;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class CustomItemsImpl extends ImplementationAdapter {

    public final static String PREFIX = "cui";

    public CustomItemsImpl() {
        super(PREFIX,"CustomItems");
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if(!isEnabled()) return null;
        return CustomItemsAPI.Companion.getCustomItem(id) == null ? null : CustomItemsAPI.Companion.getCustomItem(id);
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if(!isEnabled()) return null;
        if(CustomItemsAPI.Companion.isCustomItem(itemStack)){
            return getType().toLowerCase(Locale.ENGLISH) + ":" + CustomItemsAPI.Companion.getCustomItemID(itemStack);
        }
        return null;
    }

    @Override
    public String getAdapterId(Block block) {
        if(!isEnabled()) return null;
        String cuiID = CustomItemsAPI.Companion.getCustomItemIDAtBlock(block);
        if(cuiID != null){
            return getType().toLowerCase(Locale.ENGLISH) + ":" + cuiID;
        }
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        if(!isEnabled()) return false;

        return CustomItemsAPI.Companion.getCustomItem(id) != null;
    }

    @Override
    public String computeAdapterId(String itemId) {
        if(!isEnabled()) return null;
        ItemStack item = getAdapterItem(itemId);
        if(item == null) return null;
        return NBT.itemStackToNBT(item).toString();
    }
}
