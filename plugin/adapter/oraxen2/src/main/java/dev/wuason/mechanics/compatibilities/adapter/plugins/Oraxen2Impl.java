package dev.wuason.mechanics.compatibilities.adapter.plugins;

import dev.wuason.mechanics.compatibilities.plugins.Oraxen;
import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.api.OraxenFurniture;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class Oraxen2Impl extends ImplAdapter {
    public final static String TYPE = "or";

    public Oraxen2Impl() {
        super(Oraxen.I, TYPE);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if (!getPluginCompatibility().isEnabled()) return null;
        return OraxenItems.exists(id) ? OraxenItems.getItemById(id).build() : null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if (!getPluginCompatibility().isEnabled()) return null;
        if (OraxenItems.exists(itemStack)) {
            return convert(OraxenItems.getIdByItem(itemStack));
        }
        return null;
    }

    @Override
    public String getAdapterId(Block block) {
        if (!getPluginCompatibility().isEnabled()) return null;
        if (OraxenBlocks.isCustomBlock(block)) {
            return convert(OraxenBlocks.getCustomBlockMechanic(block.getLocation()).getItemID());
        } else if (OraxenFurniture.isFurniture(block.getLocation())) {
            return convert(OraxenFurniture.getFurnitureMechanic(block.getLocation()).getItemID());
        }
        return null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        if (!getPluginCompatibility().isEnabled() || !OraxenFurniture.isFurniture(entity)) return null;
        return convert(OraxenFurniture.getFurnitureMechanic(entity).getItemID());
    }

    @Override
    public boolean existItemAdapter(String id) {
        if (!getPluginCompatibility().isEnabled()) return false;
        return OraxenItems.exists(id);
    }
}
