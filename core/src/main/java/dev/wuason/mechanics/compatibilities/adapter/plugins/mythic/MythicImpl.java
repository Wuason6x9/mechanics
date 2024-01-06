package dev.wuason.mechanics.compatibilities.adapter.plugins.mythic;

import dev.wuason.mechanics.compatibilities.adapter.Implementation;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class MythicImpl extends Implementation {
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
    public String getAdapterID(ItemStack itemStack){
        if(isEnabled()){
            if(MythicBukkit.inst().getItemManager().isMythicItem(itemStack)){
                return getType().toLowerCase(Locale.ENGLISH) + ":" + MythicBukkit.inst().getItemManager().getMythicTypeFromItem(itemStack);
            }
        }
        return null;
    }

    @Override
    public String getAdapterID(Block block) {
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        if(isEnabled()){
            return MythicBukkit.inst().getItemManager().getItem(id).orElse(null) != null;
        }
        return false;
    }

}
