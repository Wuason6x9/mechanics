package dev.wuason.mechanics.compatibilities.adapter.plugins;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.CustomStack;
import dev.wuason.mechanics.compatibilities.plugins.ItemsAdder;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class ItemsAdderImpl extends ImplAdapter {

    public final static String TYPE = "ia";

    public ItemsAdderImpl() {
        super(ItemsAdder.I, TYPE);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if (!check()) return null;
        if(!getPluginCompatibility().isEnabled()) return null;
        CustomStack customStack = CustomStack.getInstance(id);
        return customStack != null ? customStack.getItemStack() : null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if (!check()) return null;
        if(!getPluginCompatibility().isEnabled()) return null;
        CustomStack customStack = CustomStack.byItemStack(itemStack);
        return customStack != null ? convert(customStack.getNamespacedID()) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        if (!check()) return null;
        if(!getPluginCompatibility().isEnabled()) return null;
        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
        return customBlock != null ? convert(customBlock.getNamespacedID()) : null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        if (!check()) return null;
        if(!getPluginCompatibility().isEnabled()) return null;
        CustomFurniture customFurniture = CustomFurniture.byAlreadySpawned(entity);
        return customFurniture != null ? customFurniture.getNamespacedID() : null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        if (!check()) return false;
        return getAdapterItem(id) != null;
    }
}
