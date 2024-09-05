package dev.wuason.mechanics.compatibilities.adapter.plugins;

import dev.wuason.storagemechanic.StorageMechanic;
import dev.wuason.storagemechanic.customitems.CustomItemsManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class StorageMechanicImpl extends ImplAdapter {
    public final static String TYPE = "sm";

    public StorageMechanicImpl() {
        super(dev.wuason.mechanics.compatibilities.plugins.StorageMechanic.I, TYPE);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if (!check()) return null;
        if (getPluginCompatibility().isEnabled()) {
            CustomItemsManager customItemManager = StorageMechanic.getInstance().getManagers().getCustomItemsManager();
            if (customItemManager.customItemExists(id)) {
                return customItemManager.getCustomItemById(id).getItemStack();
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if (!check()) return null;
        if (getPluginCompatibility().isEnabled()) {
            CustomItemsManager customItemManager = StorageMechanic.getInstance().getManagers().getCustomItemsManager();
            if (customItemManager.isCustomItemItemStack(itemStack)) {
                return convert(customItemManager.getCustomItemIdFromItemStack(itemStack));
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(Block block) {
        if (!check()) return null;
        if (getPluginCompatibility().isEnabled()) {
            CustomItemsManager customItemManager = StorageMechanic.getInstance().getManagers().getCustomItemsManager();
            if (customItemManager.isCustomItem(block)) {
                return convert(customItemManager.getCustomItemIdFromBlock(block));
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        return null; // StorageMechanic does not support entities
    }

    @Override
    public boolean existItemAdapter(String id) {
        if (!check()) return false;
        if (!getPluginCompatibility().isEnabled()) return false;
        return StorageMechanic.getInstance().getManagers().getCustomItemsManager().customItemExists(id);
    }
}
