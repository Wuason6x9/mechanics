package dev.wuason.mechanics.compatibilities.adapter.plugins;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public class MythicCrucibleImpl extends ImplAdapter {
    public final static String TYPE = "MythicCrucible";
    private ImplAdapter mythicCrucibleImpl;
    private boolean checked = false;
    private boolean enabled = false;

    public MythicCrucibleImpl() {
        super(dev.wuason.mechanics.compatibilities.plugins.MythicCrucible.I, TYPE);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if (!check()) return null;
        return mythicCrucibleImpl.getAdapterItem(id);
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if (!check()) return null;
        return mythicCrucibleImpl.getAdapterId(itemStack);
    }

    @Override
    public String getAdapterId(Block block) {
        if (!check()) return null;
        return mythicCrucibleImpl.getAdapterId(block);
    }

    @Override
    public String getAdapterId(Entity entity) {
        if (!check()) return null;
        return mythicCrucibleImpl.getAdapterId(entity);
    }

    @Override
    public boolean existItemAdapter(String id) {
        if (!check()) return false;
        return mythicCrucibleImpl.existItemAdapter(id);

    }

    @Override
    public void onEnable() {
        Class<?> mythicCrucible = null;
        try {
            mythicCrucible = Class.forName("dev.wuason.mechanics.compatibilities.adapter.plugins.MythicCrucible2Impl");
            mythicCrucibleImpl = (ImplAdapter) mythicCrucible.getDeclaredConstructors()[0].newInstance();
        } catch (ClassNotFoundException e) {
            enabled = false;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
