package dev.wuason.mechanics.compatibilities.adapter;


import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class VanillaImpl extends ImplementationAdapter {
    public VanillaImpl() {
        super("mc","Minecraft");
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if(id.contains("{") && id.contains("}")) return NBT.itemStackFromNBT(NBT.parseNBT(id));
        if(Material.getMaterial(id.toUpperCase(Locale.ENGLISH),false) == null) return null;
        return new ItemStack(Material.valueOf(id.toUpperCase(Locale.ENGLISH)));
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        return null;
    }

    @Override
    public String getAdapterId(Block block) {
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        return getAdapterItem(id) != null;
    }

    @Override
    public String computeAdapterId(String itemId) {
        if(itemId.contains("{") && itemId.contains("}")) return itemId;
        return NBT.itemStackToNBT(new ItemStack(Material.valueOf(itemId.toUpperCase(Locale.ENGLISH)))).toString();
    }


}
