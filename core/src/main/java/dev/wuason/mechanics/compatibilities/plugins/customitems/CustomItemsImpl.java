package dev.wuason.mechanics.compatibilities.plugins.customitems;

import com.jojodmo.customitems.api.CustomItemsAPI;
import dev.wuason.mechanics.compatibilities.Implementation;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class CustomItemsImpl extends Implementation {
    public CustomItemsImpl() {
        super("cui","CustomItems");
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if(!isEnabled()) return null;
        return CustomItemsAPI.getCustomItem(id) == null ? null : CustomItemsAPI.getCustomItem(id);
    }

    @Override
    public String getAdapterID(ItemStack itemStack) {
        if(!isEnabled()) return null;
        if(CustomItemsAPI.isCustomItem(itemStack)){
            return getType().toLowerCase(Locale.ENGLISH) + ":" + CustomItemsAPI.getCustomItemID(itemStack);
        }
        return null;
    }

    @Override
    public String getAdapterID(Block block) {
        if(!isEnabled()) return null;
        String cuiID = CustomItemsAPI.getCustomItemIDAtBlock(block);
        if(cuiID != null){
            return getType().toLowerCase(Locale.ENGLISH) + ":" + cuiID;
        }
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        if(!isEnabled()) return false;

        return CustomItemsAPI.getCustomItem(id) != null;
    }
}
