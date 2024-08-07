package dev.wuason.mechanics.compatibilities.adapter.plugins.mythic;

import de.tr7zw.changeme.nbtapi.NBT;
import dev.wuason.mechanics.compatibilities.adapter.ImplementationAdapter;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class MythicImpl extends ImplementationAdapter {
    public MythicImpl(){
        super("mythic","MythicMobs");
    }

    @Override
    public ItemStack getAdapterItem(String id){
        if(isEnabled()){
            MythicItem mythicItem = MythicBukkit.inst().getItemManager().getItem(id).get();
            if(mythicItem != null){
                return BukkitAdapter.adapt(mythicItem.generateItemStack(1));
            }
        }
        return null;
    }
    @Override
    public String getAdapterId(ItemStack itemStack){
        if(isEnabled()){
            if(MythicBukkit.inst().getItemManager().isMythicItem(itemStack)){
                return getType().toLowerCase(Locale.ENGLISH) + ":" + MythicBukkit.inst().getItemManager().getMythicTypeFromItem(itemStack);
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(Block block) {
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        if(isEnabled()){
            return MythicBukkit.inst().getItemManager().getItem(id).orElse(null) != null;
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
