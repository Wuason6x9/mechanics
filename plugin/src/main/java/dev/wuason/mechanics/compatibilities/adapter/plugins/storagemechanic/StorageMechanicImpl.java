package dev.wuason.mechanics.compatibilities.adapter.plugins.storagemechanic;

import de.tr7zw.changeme.nbtapi.NBT;
import dev.wuason.mechanics.compatibilities.adapter.ImplementationAdapter;
import dev.wuason.storagemechanic.StorageMechanic;
import dev.wuason.storagemechanic.customitems.CustomItemsManager;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class StorageMechanicImpl extends ImplementationAdapter {
    public StorageMechanicImpl() {
        super("sm","StorageMechanic");
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if(isEnabled()){
            CustomItemsManager customItemManager = StorageMechanic.getInstance().getManagers().getCustomItemsManager();
            if(customItemManager.customItemExists(id)){
                return customItemManager.getCustomItemById(id).getItemStack();
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if(isEnabled()){
            CustomItemsManager customItemManager = StorageMechanic.getInstance().getManagers().getCustomItemsManager();
            if(customItemManager.isCustomItemItemStack(itemStack)){
                return getType().toLowerCase(Locale.ENGLISH) + ":" + customItemManager.getCustomItemIdFromItemStack(itemStack);
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(Block block) {
        if(isEnabled()){
            CustomItemsManager customItemManager = StorageMechanic.getInstance().getManagers().getCustomItemsManager();
            if(customItemManager.isCustomItem(block)){
                return getType().toLowerCase(Locale.ENGLISH) + ":" + customItemManager.getCustomItemIdFromBlock(block);
            }
        }
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        return StorageMechanic.getInstance().getManagers().getCustomItemsManager().customItemExists(id);
    }

    @Override
    public String computeAdapterId(String itemId) {
        if(!isEnabled()) return null;
        ItemStack item = getAdapterItem(itemId);
        if(item == null) return null;
        return NBT.itemStackToNBT(item).toString();
    }
}
