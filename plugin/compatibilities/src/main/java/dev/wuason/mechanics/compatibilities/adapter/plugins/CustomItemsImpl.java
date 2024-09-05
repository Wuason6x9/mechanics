package dev.wuason.mechanics.compatibilities.adapter.plugins;

import dev.wuason.customItemsAPI.CustomItemsAPI;
import dev.wuason.mechanics.compatibilities.plugins.CustomItems;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class CustomItemsImpl extends ImplAdapter {

    public final static String TYPE = "cui";

    public CustomItemsImpl() {
        super(CustomItems.I, TYPE);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if (!check()) return null;
        if (!getPluginCompatibility().isEnabled()) return null;
        return CustomItemsAPI.Companion.getCustomItem(id) == null ? null : CustomItemsAPI.Companion.getCustomItem(id);
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if (!check()) return null;
        if (!getPluginCompatibility().isEnabled()) return null;
        return CustomItemsAPI.Companion.isCustomItem(itemStack) ? convert(CustomItemsAPI.Companion.getCustomItemID(itemStack)) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        if (!check()) return null;
        if (!getPluginCompatibility().isEnabled()) return null;
        String cuiId = CustomItemsAPI.Companion.getCustomItemIDAtBlock(block);
        return cuiId != null ? convert(cuiId) : null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        return null; //Not implemented
    }

    @Override
    public boolean existItemAdapter(String id) {
        if (!check()) return false;
        if (!getPluginCompatibility().isEnabled()) return false;
        return CustomItemsAPI.Companion.getCustomItem(id) != null;
    }
}
