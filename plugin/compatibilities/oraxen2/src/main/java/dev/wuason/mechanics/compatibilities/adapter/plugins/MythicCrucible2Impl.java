package dev.wuason.mechanics.compatibilities.adapter.plugins;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.utils.jnbt.CompoundTag;
import io.lumine.mythiccrucible.MythicCrucible;
import io.lumine.mythiccrucible.items.CrucibleItem;
import io.lumine.mythiccrucible.items.blocks.CustomBlockItemContext;
import io.lumine.mythiccrucible.items.furniture.Furniture;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class MythicCrucible2Impl extends ImplAdapter {
    public final static String TYPE = "MythicCrucible";

    public MythicCrucible2Impl() {
        super(dev.wuason.mechanics.compatibilities.plugins.MythicCrucible.I, TYPE);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if (getPluginCompatibility().isEnabled()) {
            CrucibleItem crucibleItem = MythicCrucible.inst().getItemManager().getItem(id).orElse(null);
            if (crucibleItem != null) {
                return BukkitAdapter.adapt(crucibleItem.getMythicItem().generateItemStack(1));
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if (getPluginCompatibility().isEnabled()) {
            CompoundTag compoundTag = MythicBukkit.inst().getVolatileCodeHandler().getItemHandler().getNBTData(itemStack);
            if (compoundTag.containsKey("MYTHIC_TYPE")) {
                return convert(compoundTag.getString("MYTHIC_TYPE"));
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(Block block) {
        if (getPluginCompatibility().isEnabled()) {
            CustomBlockItemContext customBlock = MythicCrucible.inst().getItemManager().getCustomBlockManager().getBlockFromBlock(block).orElse(null);
            if (customBlock != null) {
                return convert(MythicBukkit.inst().getVolatileCodeHandler().getItemHandler().getNBTData(customBlock.getItem()).getString("MYTHIC_TYPE"));
            }
            Furniture furniture = MythicCrucible.inst().getItemManager().getFurnitureManager().getFurniture(block).orElse(null);
            if (furniture != null) {
                return convert(furniture.getFurnitureData().getItem().getInternalName());
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        Furniture furniture = MythicCrucible.inst().getItemManager().getFurnitureManager().getFurniture(entity.getUniqueId()).orElse(null);
        return furniture != null ? convert(furniture.getFurnitureData().getItem().getInternalName()) : null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        return getPluginCompatibility().isEnabled() ? MythicCrucible.inst().getItemManager().getItemNames().contains(id) : false;
    }
}
