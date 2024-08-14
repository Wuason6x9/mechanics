package dev.wuason.mechanics.compatibilities.adapter.plugins;

import de.tr7zw.changeme.nbtapi.NBT;
import dev.wuason.mechanics.compatibilities.adapter.ImplementationAdapter;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.utils.jnbt.CompoundTag;
import io.lumine.mythiccrucible.MythicCrucible;
import io.lumine.mythiccrucible.items.CrucibleItem;
import io.lumine.mythiccrucible.items.blocks.CustomBlockItemContext;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class MythicCrucibleImpl extends ImplementationAdapter {
    public final static String PREFIX = "MythicCrucible";
    public MythicCrucibleImpl() {
        super(PREFIX,"MythicCrucible");
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if(isEnabled()){
            CrucibleItem crucibleItem = MythicCrucible.inst().getItemManager().getItem(id).orElse(null);
            if(crucibleItem != null){
                return BukkitAdapter.adapt(crucibleItem.getMythicItem().generateItemStack(1));
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if(isEnabled()){
            CompoundTag compoundTag = MythicBukkit.inst().getVolatileCodeHandler().getItemHandler().getNBTData(itemStack);
            if(compoundTag.containsKey("MYTHIC_TYPE")){
                return getType().toLowerCase(Locale.ENGLISH) + ":" + compoundTag.getString("MYTHIC_TYPE");
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(Block block) {
        if(isEnabled()){
            CustomBlockItemContext customBlock = MythicCrucible.inst().getItemManager().getCustomBlockManager().getBlockFromBlock(block).orElse(null);
            if(customBlock != null){
                return getType().toLowerCase(Locale.ENGLISH) + ":" + MythicBukkit.inst().getVolatileCodeHandler().getItemHandler().getNBTData(customBlock.getItem()).getString("MYTHIC_TYPE");
            }
        }
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        if(isEnabled()){
            return MythicCrucible.inst().getItemManager().getItemNames().contains(id);
        }
        return false;
    }

    @Override
    public String computeAdapterId(String itemId) {
        if(!isEnabled()) return null;
        ItemStack item = getAdapterItem(itemId);
        if(item == null) return null;
        return NBT.itemStackToNBT(item).toString();
    }
}
