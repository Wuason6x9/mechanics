package dev.wuason.mechanics.compatibilities.adapter.plugins;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;
import com.ssomar.score.api.executableitems.config.ExecutableItemsManagerInterface;
import dev.wuason.mechanics.compatibilities.plugins.ExecutableItems;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ExecutableItemsImpl extends ImplAdapter {
    public final static String TYPE = "ei";
    public ExecutableItemsImpl() {
        super(ExecutableItems.I, TYPE);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if (!check()) return null;
        if(!getPluginCompatibility().isEnabled()) return null;
        ExecutableItemsManagerInterface executableItemsManager = ExecutableItemsAPI.getExecutableItemsManager();
        if(!executableItemsManager.isValidID(id)) return null;
        ExecutableItemInterface executableItem = executableItemsManager.getExecutableItem(id).orElse(null);
        if(executableItem == null) return null;
        return executableItem.buildItem(1, Optional.empty());
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if (!check()) return null;
        if(!getPluginCompatibility().isEnabled()) return null;
        ExecutableItemsManagerInterface executableItemsManagerInterface = ExecutableItemsAPI.getExecutableItemsManager();
        ExecutableItemInterface executableItemInterface = executableItemsManagerInterface.getExecutableItem(itemStack).orElse(null);
        return executableItemInterface != null ? convert(executableItemInterface.getId()) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        return null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        return null; // Not supported
    }

    @Override
    public boolean existItemAdapter(String id) {
        if (!check()) return false;
        if(!getPluginCompatibility().isEnabled()) return false;
        return ExecutableItemsAPI.getExecutableItemsManager().isValidID(id);
    }
}
