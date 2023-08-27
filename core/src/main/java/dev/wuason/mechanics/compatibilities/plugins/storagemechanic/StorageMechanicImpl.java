package dev.wuason.mechanics.compatibilities.plugins.storagemechanic;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.compatibilities.Implementation;
import dev.wuason.mechanics.mechanics.Mechanic;
import dev.wuason.storagemechanic.StorageMechanic;
import dev.wuason.storagemechanic.customblocks.CustomBlockManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class StorageMechanicImpl extends Implementation {
    public StorageMechanicImpl() {
        super("sm","StorageMechanic");
    }

    @Override
    public ItemStack getAdapterItem(String id) {

        if(isEnabled()){
            CustomBlockManager customBlockManager = StorageMechanic.getInstance().getManagers().getCustomBlockManager();
            if(customBlockManager.customBlockExists(id)){
                return customBlockManager.getCustomBlockById(id).getItemStack();
            }
        }
        return null;
    }

    @Override
    public String getAdapterID(ItemStack itemStack) {
        if(isEnabled()){
            CustomBlockManager customBlockManager = StorageMechanic.getInstance().getManagers().getCustomBlockManager();
            if(customBlockManager.isCustomBlockItemStack(itemStack)){
                return getType().toLowerCase(Locale.ENGLISH) + ":" + customBlockManager.getCustomBlockIdFromItemStack(itemStack);
            }
        }
        return null;
    }

    @Override
    public String getAdapterID(Block block) {
        if(isEnabled()){
            CustomBlockManager customBlockManager = StorageMechanic.getInstance().getManagers().getCustomBlockManager();
            if(customBlockManager.isCustomBlock(block)){
                return getType().toLowerCase(Locale.ENGLISH) + ":" + customBlockManager.getCustomBlockIdFromBlock(block);
            }
        }
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        return StorageMechanic.getInstance().getManagers().getCustomBlockManager().customBlockExists(id);
    }

    @Override
    public boolean isEnabled() {
        if(!super.isEnabled()){
            Mechanic mechanic = Mechanics.getInstance().getManager().getMechanicsManager().getMechanic(super.getPluginName().toLowerCase(Locale.ENGLISH));
            if(mechanic != null){
                super.setEnabled(mechanic.getPlugin().isEnabled());
            }
        }
        return super.isEnabled();
    }
}
