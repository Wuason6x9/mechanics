package dev.wuason.mechanics.compatibilities;

import de.tr7zw.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class VanillaImpl extends Implementation{
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
    public String getAdapterID(ItemStack itemStack) {
        return null;
    }

    @Override
    public String getAdapterID(Block block) {
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        return getAdapterItem(id) != null;
    }
}
