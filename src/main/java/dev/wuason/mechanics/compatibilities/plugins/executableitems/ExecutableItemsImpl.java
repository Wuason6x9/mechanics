package dev.wuason.mechanics.compatibilities.plugins.executableitems;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;
import com.ssomar.score.api.executableitems.config.ExecutableItemsManagerInterface;
import dev.wuason.mechanics.compatibilities.Implementation;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.Optional;

public class ExecutableItemsImpl extends Implementation {
    public ExecutableItemsImpl() {
        super("ei","ExecutableItems");
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if(!isEnabled()) return null;
        ExecutableItemsManagerInterface executableItemsManager = ExecutableItemsAPI.getExecutableItemsManager();
        if(!executableItemsManager.isValidID(id)) return null;
        ExecutableItemInterface executableItem = executableItemsManager.getExecutableItem(id).orElse(null);
        if(executableItem == null) return null;
        return executableItem.buildItem(1, Optional.of(null));
    }

    @Override
    public String getAdapterID(ItemStack itemStack) {
        if(!isEnabled()) return null;
        ExecutableItemsManagerInterface executableItemsManagerInterface = ExecutableItemsAPI.getExecutableItemsManager();
        ExecutableItemInterface executableItemInterface = executableItemsManagerInterface.getExecutableItem(itemStack).orElse(null);
        if(executableItemInterface != null){
            return getType().toLowerCase(Locale.ENGLISH) + ":" + executableItemInterface.getId();
        }
        return null;
    }

    @Override
    public String getAdapterID(Block block) {
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        if(!isEnabled()) return false;
        return ExecutableItemsAPI.getExecutableItemsManager().isValidID(id);
    }
}
