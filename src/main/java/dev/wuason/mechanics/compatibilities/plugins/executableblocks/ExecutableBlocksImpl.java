package dev.wuason.mechanics.compatibilities.plugins.executableblocks;

import com.ssomar.executableblocks.api.ExecutableBlocksAPI;
import com.ssomar.executableblocks.executableblocks.ExecutableBlock;
import com.ssomar.executableblocks.executableblocks.ExecutableBlocksManager;
import com.ssomar.executableblocks.executableblocks.placedblocks.ExecutableBlockPlaced;
import dev.wuason.mechanics.compatibilities.Implementation;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.Optional;

public class ExecutableBlocksImpl extends Implementation {
    public ExecutableBlocksImpl() {
        super("eb","ExecutableBlocks");
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if(!isEnabled()) return null;
        ExecutableBlocksManager executableBlocksManager = ExecutableBlocksAPI.getExecutableBlocksManager();
        if(!executableBlocksManager.isValidID(id)) return null;
        ExecutableBlock executableBlock = executableBlocksManager.getExecutableBlock(id).orElse(null);
        if(executableBlock == null) return null;
        return executableBlock.buildItem(1, Optional.of(null));
    }

    @Override
    public String getAdapterID(ItemStack itemStack) {
        if(!isEnabled()) return null;
        ExecutableBlock executableBlock = ExecutableBlocksAPI.getExecutableBlocksManager().getExecutableBlock(itemStack).orElse(null);
        if(executableBlock != null) {
            return getType().toLowerCase(Locale.ENGLISH) + ":" + executableBlock.getId();
        }
        return null;
    }

    @Override
    public String getAdapterID(Block block) {
        if(!isEnabled()) return null;
        ExecutableBlockPlaced executableBlockPlaced = ExecutableBlocksAPI.getExecutableBlocksPlacedManager().getExecutableBlockPlaced(block).orElse(null);
        if(executableBlockPlaced != null){
            return getType().toLowerCase(Locale.ENGLISH) + ":" + executableBlockPlaced.getEB_ID();
        }
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        if(!isEnabled()) return false;
        return ExecutableBlocksAPI.getExecutableBlocksManager().isValidID(id);
    }
}
