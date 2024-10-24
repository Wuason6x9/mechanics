package dev.wuason.mechanics.compatibilities.adapter.plugins;

import com.saicone.rtag.item.ItemTagStream;
import dev.wuason.mechanics.compatibilities.plugins.Minecraft;
import dev.wuason.mechanics.utils.VersionDetector;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public class VanillaImpl extends ImplAdapter {
    public final static String TYPE = "mc";
    private Method toStringCompound;
    public VanillaImpl() {
        super(Minecraft.I, TYPE);
        if (VersionDetector.getServerVersion().isAtLeast(VersionDetector.ServerVersion.v1_20_6)) {
            try {
                Class<?> itemMetaClass = Class.forName("org.bukkit.inventory.meta.ItemMeta");
                toStringCompound = itemMetaClass.getDeclaredMethod("getAsComponentString");
                toStringCompound.setAccessible(true);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        String[] nbtData = getNBTData(id);
        if(Material.getMaterial(nbtData[0].toUpperCase(Locale.ENGLISH),false) == null) return null;
        String item = "minecraft:" + nbtData[0] + (nbtData[1] == null ? "" : nbtData[1]);
        ItemStack itemStack = Bukkit.getItemFactory().createItemStack(item);
        return itemStack;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        String nbt = getNBTData(itemStack);
        return convert(itemStack.getType().name().toLowerCase(Locale.ENGLISH)) + ((nbt.isEmpty() || nbt.equals("{}") || nbt.equals("[]")) ? "" : ":" + nbt);
    }

    @Override
    public String getAdapterId(Block block) {
        String blockState = block.getBlockData().getAsString().replace(block.getType().getKey().toString(), "");
        return convert(block.getType().name().toLowerCase(Locale.ENGLISH)) + (blockState.isEmpty() || blockState.equals("[]") ? "" : ":" + blockState);
    }

    @Override
    public String getAdapterId(Entity entity) {
        return entity.getType() == EntityType.DROPPED_ITEM ? convert(((Item) entity).getItemStack().getType().name()) : null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        return getAdapterItem(id) != null;
    }

    private String[] getNBTData(String line) {
        String[] data = {line.toLowerCase(Locale.ENGLISH), null};
        if (line.contains(":")) {
            data[0] = line.substring(0, line.indexOf(":")).toLowerCase(Locale.ENGLISH);
            if (line.length() == line.indexOf(":") + 1) return data;
            String nbt = line.substring(line.indexOf(":") + 1);
            if (!nbt.isBlank()) {
                data[1] = nbt;
            }
        }
        return data;
    }

    private String getNBTData(ItemStack itemStack) {
        if (toStringCompound != null && itemStack.hasItemMeta()) {
            try {
                return (String) toStringCompound.invoke(itemStack.getItemMeta());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else if (itemStack.hasItemMeta()) {
            return itemStack.getItemMeta().getAsString();
        }
        return "{}";
    }
}
