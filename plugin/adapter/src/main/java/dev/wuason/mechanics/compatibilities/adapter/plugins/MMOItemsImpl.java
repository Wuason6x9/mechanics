package dev.wuason.mechanics.compatibilities.adapter.plugins;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ItemTier;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.manager.BlockManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class MMOItemsImpl extends ImplAdapter {

    public final static String TYPE = "mmoitems";

    public MMOItemsImpl() {
        super(dev.wuason.mechanics.compatibilities.plugins.MMOItems.I, TYPE);
    }

    @Override
    public ItemStack getAdapterItem(String itemID) {
        if (!check()) return null;
        if (!getPluginCompatibility().isEnabled()) return null;
        String[] src = itemID.split(":"); //example TYPE:ID(:LEVEL:TIER) OPTIONAL
        //TYPE
        Type mmoType = MMOItems.plugin.getTypes().get(src[0].toUpperCase());
        if (mmoType == null) return null;
        //ID
        String itemMMoId = src[1].toUpperCase(Locale.ENGLISH);
        MMOItem mmoItem = null;
        //ONLY ID & TYPE
        if (src.length < 3) {
            mmoItem = MMOItems.plugin.getMMOItem(mmoType, itemMMoId);
        }
        // ID & TYPE & LEVEL & TIER
        if (src.length < 5 && src.length > 2) {
            if (isNumber(src[2])) {
                int mmoLevel = Integer.parseInt(src[2]);
                ItemTier itemTier = MMOItems.plugin.getTiers().get(src[3]);
                if (itemTier == null) return null;
                mmoItem = MMOItems.plugin.getMMOItem(mmoType, itemMMoId, mmoLevel, itemTier);
            }
        }
        if (mmoItem == null) return null;
        return mmoItem.newBuilder().build();
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if (!check()) return null;
        if (!getPluginCompatibility().isEnabled()) return null;
        Type type = MMOItems.getType(itemStack);
        String mmoItemId = MMOItems.getID(itemStack);
        return (type != null && mmoItemId != null) ? convert(type.getId() + ":" + mmoItemId) : null;
    }

    @Override
    public String getAdapterId(Block block) {
        if (!check()) return null;
        if (!getPluginCompatibility().isEnabled()) return null;
        BlockManager blockManager = MMOItems.plugin.getCustomBlocks();
        if (blockManager.isMushroomBlock(block.getType())) {
            net.Indyuce.mmoitems.api.block.CustomBlock customBlock = blockManager.getFromBlock(block.getBlockData()).orElse(null);
            if (customBlock != null) {
                return getAdapterId(customBlock.getItem());
            }
        }
        return null;
    }

    @Override
    public String getAdapterId(Entity entity) {
        return null; //Not supported
    }

    @Override
    public boolean existItemAdapter(String id) {
        if (!check()) return false;
        if (!getPluginCompatibility().isEnabled()) return false;
        String[] src = id.split(":");
        Type type = MMOItems.plugin.getTypes().get(src[0].toUpperCase());
        return type != null && MMOItems.plugin.getMMOItem(type, src[1].toUpperCase(Locale.ENGLISH)) != null;
    }

    private boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
