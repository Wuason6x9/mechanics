package dev.wuason.mechanics.compatibilities.adapter.plugins;

import dev.wuason.mechanics.compatibilities.plugins.Oraxen;
import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.api.OraxenFurniture;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public class OraxenImpl extends ImplAdapter {

    public final static String TYPE = "or";

    private ImplAdapter oraxen2Impl;

    public OraxenImpl() {
        super(Oraxen.I, TYPE);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if (!check()) return null;
        if (oraxen2Impl != null) {
            return oraxen2Impl.getAdapterItem(id);
        }
        if (!getPluginCompatibility().isEnabled()) return null;
        return OraxenItems.exists(id) ? OraxenItems.getItemById(id).build() : null;
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if (!check()) return null;
        if (oraxen2Impl != null) {
            return oraxen2Impl.getAdapterId(itemStack);
        }
        if (!getPluginCompatibility().isEnabled()) return null;
        if (OraxenItems.exists(itemStack)) {
            return convert(OraxenItems.getIdByItem(itemStack));
        }
        return null;
    }

    @Override
    public String getAdapterId(Block block) {
        if (!check()) return null;
        if (oraxen2Impl != null) {
            return oraxen2Impl.getAdapterId(block);
        }
        if (!getPluginCompatibility().isEnabled()) return null;
        if (OraxenBlocks.isOraxenBlock(block)) {
            return convert(OraxenBlocks.getOraxenBlock(block.getBlockData()).getItemID());
        } else if (OraxenFurniture.isFurniture(block)) {
            return convert(OraxenFurniture.getFurnitureMechanic(block).getItemID());
        }
        return null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        if (!check()) return null;
        if (oraxen2Impl != null) {
            return oraxen2Impl.getAdapterId(entity);
        }
        if (!getPluginCompatibility().isEnabled() || !OraxenFurniture.isFurniture(entity)) return null;
        return convert(OraxenFurniture.getFurnitureMechanic(entity).getItemID());
    }

    @Override
    public boolean existItemAdapter(String id) {
        if (!check()) return false;
        if (oraxen2Impl != null) {
            return oraxen2Impl.existItemAdapter(id);
        }
        if(!getPluginCompatibility().isEnabled()) return false;
        return OraxenItems.exists(id);
    }

    @Override
    public void onEnable() {
        if (Oraxen.I.isOraxen2()) {
            try {
                Class<?> oraxen2 = Class.forName("dev.wuason.mechanics.compatibilities.adapter.plugins.Oraxen2Impl");
                oraxen2Impl = (ImplAdapter) oraxen2.getDeclaredConstructors()[0].newInstance();
            } catch (ClassNotFoundException e) {
                oraxen2Impl = null;
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
