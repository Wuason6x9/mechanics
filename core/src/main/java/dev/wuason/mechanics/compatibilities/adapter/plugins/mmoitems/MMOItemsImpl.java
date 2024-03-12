package dev.wuason.mechanics.compatibilities.adapter.plugins.mmoitems;

import de.tr7zw.changeme.nbtapi.NBT;
import dev.wuason.mechanics.compatibilities.adapter.ImplementationAdapter;
import dev.wuason.mechanics.utils.Utils;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ItemTier;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.manager.BlockManager;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class MMOItemsImpl extends ImplementationAdapter {
    public MMOItemsImpl() {
        super("mmoitems","MMOItems");
    }

    @Override
    public ItemStack getAdapterItem(String itemID) {
        if(!isEnabled()) return null;
        String[] src = itemID.split(":"); //example TYPE:ID(:LEVEL:TIER) OPTIONAL
        //TYPE
        Type mmoType = MMOItems.plugin.getTypes().get(src[0].toUpperCase());
        if(mmoType == null) return null;
        //ID
        String itemMMoId = src[1];
        MMOItem mmoItem = null;
        //ONLY ID & TYPE
        if(src.length<3){
            mmoItem = MMOItems.plugin.getMMOItem(mmoType,itemMMoId);
        }
        // ID & TYPE & LEVEL & TIER
        if(src.length<5 && src.length>2){
            if(Utils.isNumber(src[2])){
                int mmoLevel = Integer.parseInt(src[2]);
                ItemTier itemTier = MMOItems.plugin.getTiers().get(src[3]);
                if(itemTier == null) return null;
                mmoItem = MMOItems.plugin.getMMOItem(mmoType,itemMMoId,mmoLevel,itemTier);
            }
        }
        if(mmoItem == null) return null;
        return mmoItem.newBuilder().build();
    }

    @Override
    public String getAdapterId(ItemStack itemStack) {
        if(!isEnabled()) return null;
        Type type = MMOItems.getType(itemStack);
        String mmoItemId = MMOItems.getID(itemStack);
        if(type != null && mmoItemId != null) {
            return getType().toLowerCase(Locale.ENGLISH) + ":" + type.getId().toLowerCase() + ":" + mmoItemId;
        }
        return null;
    }

    @Override
    public String getAdapterId(Block block) {
        if(!isEnabled()) return null;
        BlockManager blockManager = MMOItems.plugin.getCustomBlocks();
        if(blockManager.isMushroomBlock(block.getType())){
            net.Indyuce.mmoitems.api.block.CustomBlock customBlock = blockManager.getFromBlock(block.getBlockData()).orElse(null);
            if(customBlock != null){
                return getAdapterId(customBlock.getItem());
            }
        }
        return null;
    }

    @Override
    public boolean existItemAdapter(String id) {
        if(!isEnabled()) return false;
        String[] src = id.split(":");
        Type type = MMOItems.plugin.getTypes().get(src[0].toUpperCase());
        if(type == null) return false;
        return MMOItems.plugin.getMMOItem(type,src[1]) != null;
    }

    @Override
    public String computeAdapterId(String itemId) {
        if(!isEnabled()) return null;
        ItemStack item = getAdapterItem(itemId);
        if(item == null) return null;
        return NBT.itemStackToNBT(item).toString();
    }
}
