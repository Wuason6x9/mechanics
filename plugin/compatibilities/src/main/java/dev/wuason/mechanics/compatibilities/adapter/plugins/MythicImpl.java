package dev.wuason.mechanics.compatibilities.adapter.plugins;

import dev.wuason.mechanics.compatibilities.plugins.MythicMobs;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class MythicImpl extends ImplAdapter {
    public final static String TYPE = "mythic";

    public MythicImpl() {
        super(MythicMobs.I, TYPE);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if (!check()) return null;
        if (getPluginCompatibility().isEnabled()) {
            MythicItem mythicItem = MythicBukkit.inst().getItemManager().getItem(id).get();
            if (mythicItem != null) {
                return BukkitAdapter.adapt(mythicItem.generateItemStack(1));
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if (!check()) return null;
        if (getPluginCompatibility().isEnabled()) {
            if (MythicBukkit.inst().getItemManager().isMythicItem(itemStack)) {
                return convert(MythicBukkit.inst().getItemManager().getMythicTypeFromItem(itemStack));
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(Block block) {
        return null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        if (!check()) return false;
        if (getPluginCompatibility().isEnabled()) {
            return MythicBukkit.inst().getItemManager().getItem(id).orElse(null) != null;
        }
        return false;
    }

}
