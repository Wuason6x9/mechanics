package dev.wuason.mechanics.compatibilities.adapter.plugins;

import com.ssomar.executableblocks.api.ExecutableBlocksAPI;
import com.ssomar.executableblocks.executableblocks.ExecutableBlock;
import com.ssomar.executableblocks.executableblocks.ExecutableBlocksManager;
import com.ssomar.executableblocks.executableblocks.placedblocks.ExecutableBlockPlaced;
import dev.wuason.mechanics.compatibilities.plugins.ExecutableBlocks;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class ExecutableBlocksImpl extends ImplAdapter {
    public final static String TYPE = "eb";

    public ExecutableBlocksImpl() {
        super(ExecutableBlocks.I, TYPE);
    }

    @Override
    public ItemStack getAdapterItem(String id) {
        if (!check()) return null;
        if (!getPluginCompatibility().isEnabled()) return null;
        ExecutableBlocksManager executableBlocksManager = ExecutableBlocksAPI.getExecutableBlocksManager();
        if (!executableBlocksManager.isValidID(id)) return null;
        ExecutableBlock executableBlock = executableBlocksManager.getExecutableBlock(id).orElse(null);
        if (executableBlock == null) return null;
        return executableBlock.buildItem(1, null, null);
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if (!check()) return null;
        if (!getPluginCompatibility().isEnabled()) return null;
        ExecutableBlock executableBlock = ExecutableBlocksAPI.getExecutableBlocksManager().getExecutableBlock(itemStack).orElse(null);
        return executableBlock != null ? convert(executableBlock.getId()) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        if (!check()) return null;
        if (!getPluginCompatibility().isEnabled()) return null;
        ExecutableBlockPlaced executableBlockPlaced = ExecutableBlocksAPI.getExecutableBlocksPlacedManager().getExecutableBlockPlaced(block).orElse(null);
        return executableBlockPlaced != null ? convert(executableBlockPlaced.getEB_ID()) : null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        return null; // ExecutableBlocks doesn't support entities now
    }

    @Override
    public boolean existItemAdapter(String id) {
        if (!check()) return false;
        if (!getPluginCompatibility().isEnabled()) return false;
        return ExecutableBlocksAPI.getExecutableBlocksManager().isValidID(id);
    }
}
