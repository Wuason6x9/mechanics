package dev.wuason.mechanics.compatibilities.adapter;

import de.tr7zw.changeme.nbtapi.NBT;
import dev.wuason.mechanics.compatibilities.adapter.plugins.CustomItemsImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.ExecutableBlocksImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.ExecutableItemsImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.ItemsAdderImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.MMOItemsImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.MythicImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.MythicCrucibleImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.OraxenImpl;
import dev.wuason.mechanics.compatibilities.adapter.plugins.StorageMechanicImpl;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Adapter {
    private static final ImplementationAdapter[] implementationAdapters = {new VanillaImpl(), new StorageMechanicImpl(), new OraxenImpl(), new MythicImpl(), new MMOItemsImpl(), new ItemsAdderImpl(), new ExecutableItemsImpl(), new ExecutableBlocksImpl(), new CustomItemsImpl(), new MythicCrucibleImpl()};

    private static HashMap<String, ImplementationAdapter> types = new HashMap<>() {{
        for (ImplementationAdapter impl : implementationAdapters) {
            put(impl.getType(), impl);
        }
    }};

    //****************************************
    //************ GET ITEMS STACK ***********
    //****************************************

    /**
     * Retrieves an ItemStack based on the given itemId.
     *
     * @param itemId the identifier of the item, in the format "type:id" (e.g. "mc:dirt")
     * @return the corresponding ItemStack, or null if it doesn't exist
     */
    public static ItemStack getItemStack(String itemId) {
        String type = itemId.substring(0, itemId.indexOf(":")).toUpperCase(Locale.ENGLISH);
        itemId = itemId.substring(itemId.indexOf(":") + 1);
        ImplementationAdapter impl = types.get(type);
        if (impl == null) return null;
        return impl.getAdapterItem(itemId);
    }

    /**
     * Retrieves a list of ItemStacks based on the given item IDs.
     *
     * @param i the list of item IDs, each in the format "type:id" (e.g. "mc:dirt")
     * @return a list of corresponding ItemStacks, or an empty list if no items exist
     */
    public static List<ItemStack> getItemsStack(List<String> i) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (String str : i) {
            itemStacks.add(getItemStack(str));
        }
        return itemStacks;
    }

    //****************************************
    //****** COMPUTE ADAPTER ID SECTION ******
    //****************************************

    /**
     * Computes the adapter ID for the given item ID.
     *
     * @param adapterId the identifier of the item, in the format "type:id" (e.g. "mc:dirt")
     * @return the adapter ID for the given item ID, or the original item ID if no adapter is applicable
     */
    public static String computeAdapterIdByString(String adapterId) {
        String type = adapterId.substring(0, adapterId.indexOf(":")).toUpperCase(Locale.ENGLISH);
        String itemId = adapterId.substring(adapterId.indexOf(":") + 1);
        ImplementationAdapter impl = types.get(type);
        if(impl == null) throw new IllegalArgumentException("Invalid type: " + type);
        return "mc:" + impl.computeAdapterId(itemId).trim();
    }


    /**
     * Computes the adapter IDs for a list of strings.
     *
     * @param i the list of strings representing item IDs in the format "type:id" (e.g. "mc:dirt")
     * @return a list of adapter IDs corresponding to the input item IDs, or an empty list if no adapters are applicable
     */
    public static List<String> computeAdapterIdsByString(List<String> i) {
        List<String> itemIds = new ArrayList<>();
        for (String str : i) {
            itemIds.add(computeAdapterIdByString(str));
        }
        return itemIds;
    }

    /**
     * Computes the adapter ID for the given ItemStack.
     *
     * @param i the input ItemStack
     * @return the adapter ID for the given ItemStack, or null if it cannot be computed
     */
    public static String computeAdapterIdByItemStack(ItemStack i) {
        ItemStack itemStack = i.clone();
        if (itemStack == null) return null;
        itemStack.setAmount(1);
        return "mc:" + NBT.itemStackToNBT(itemStack);
    }

    /**
     * Computes the adapter IDs for the given list of ItemStacks.
     *
     * @param i the list of ItemStacks
     * @return the list of adapter IDs for the given ItemStacks
     */
    public static List<String> computeAdapterIdsByItemStack(List<ItemStack> i) {
        List<String> itemIds = new ArrayList<>();
        for (ItemStack itemStack : i) {
            itemIds.add(computeAdapterIdByItemStack(itemStack));
        }
        return itemIds;
    }

    //****************************************
    //************ Is Similar Item ***********
    //****************************************

    /**
     * Determines if two items are similar.
     *
     * @param item1 the first item to compare
     * @param item2 the second item to compare
     * @return true if the items are similar, false otherwise
     */
    public static boolean isSimilar(String item1, String item2) {
        try {
            return NBT.itemStackFromNBT(NBT.parseNBT(computeAdapterIdByString(item1))).isSimilar(NBT.itemStackFromNBT(NBT.parseNBT(computeAdapterIdByString(item2))));
        } catch (Exception e) {
            return false;
        }
    }

    //****************************************
    //************  COMPARE ITEMS   **********
    //****************************************

    /**
     * Compares the given adapter ID and ItemStack to determine if they are similar.
     *
     * @param adapterId the identifier of the adapter, in the format "type:id" (e.g. "mc:dirt")
     * @param itemStack the ItemStack to compare
     * @return true if the adapter ID and ItemStack are similar, false otherwise
     */
    public static boolean compareItems(String adapterId, ItemStack itemStack) {
        return NBT.itemStackFromNBT(NBT.parseNBT(computeAdapterIdByString(adapterId))).isSimilar(itemStack);
    }

    /**
     * Compares the given list of adapters with the provided ItemStack.
     * Returns true if any of the adapters is similar to the ItemStack.
     *
     * @param adapters the list of adapter IDs to compare with
     * @param itemStack the ItemStack to compare with
     * @return true if any of the adapters is similar to the ItemStack, false otherwise
     */
    public static boolean compareItems(List<String> adapters, ItemStack itemStack) {
        for (String adapter : adapters) {
            if (compareItems(adapter, itemStack)) return true;
        }
        return false;
    }

    /**
     * Compares the basic adapter IDs of the given item and the item identified by the adapter ID.
     *
     * @param adapterId the adapter ID of the item to compare
     * @param itemStack the item to compare with
     * @return true if the basic adapter IDs of the items match, false otherwise
     */
    public static boolean compareItemsBasic(String adapterId, ItemStack itemStack) {
        return getAdapterIdBasic(getItemStack(adapterId)).equals(getAdapterIdBasic(itemStack));
    }

    /**
     * Compares the given ItemStack with a list of adapters.
     *
     * @param adapters   the list of adapter IDs to compare with
     * @param itemStack  the ItemStack to compare
     * @return true if the ItemStack matches any of the adapters, false otherwise
     */
    public static boolean compareItemsBasic(List<String> adapters, ItemStack itemStack) {
        for (String adapter : adapters) {
            if (compareItemsBasic(adapter, itemStack)) return true;
        }
        return false;
    }

    //****************************************
    //************ GET ADAPTER ID ************
    //****************************************

    /**
     * Retrieves the basic adapter ID for the given ItemStack.
     *
     * @param i the ItemStack to get the adapter ID for
     * @return the basic adapter ID for the ItemStack, or null if the ItemStack is null
     */
    public static String getAdapterIdBasic(ItemStack i) {
        if (i == null) return null;
        ItemStack itemStack = i.clone();
        itemStack.setAmount(1);
        String id = "mc:" + itemStack.getType().toString().toLowerCase(Locale.ENGLISH);
        for (ImplementationAdapter impl : types.values()) {
            String itemId = impl.getAdapterId(itemStack);
            if (itemId != null) {
                id = itemId;
                break;
            }
        }
        return id;
    }

    /**
     * Retrieves the adapter ID for the given Block using basic logic.
     *
     * @param block the Block to get the adapter ID for.
     * @return the adapter ID for the given Block, or null if the Block is null.
     */
    public static String getAdapterIdBasic(Block block) {
        if (block == null) return null;
        String id = "mc:" + block.getType().toString().toLowerCase(Locale.ENGLISH);
        for (ImplementationAdapter impl : types.values()) {
            String itemId = impl.getAdapterId(block);
            if (itemId != null) id = itemId;
        }
        return id;
    }

    public static String getAdapterIdBasic(String id){
        return getAdapterIdBasic(getItemStack(id));
    }

    /**
     * Retrieves the adapter ID for the given ItemStack.
     *
     * @param i the input ItemStack
     * @return the adapter ID for the given ItemStack, or null if it cannot be computed
     */
    public static String getAdapterId(ItemStack i) {
        if (i == null) return null;
        ItemStack itemStack = i.clone();
        itemStack.setAmount(1);
        String id = "mc:" + NBT.itemStackToNBT(itemStack);
        for (ImplementationAdapter impl : types.values()) {
            String itemId = impl.getAdapterId(itemStack);
            if (itemId != null) {
                id = itemId;
                break;
            }
        }
        return id;
    }

    /**
     * Retrieves the adapter ID for the given Block.
     *
     * @param block the Block to get the adapter ID for
     * @return the adapter ID for the given Block, or null if the Block is null
     */
    public static String getAdapterId(Block block) {
        if (block == null) return null;
        String id = "mc:" + NBT.itemStackToNBT(new ItemStack(block.getType()));
        for (ImplementationAdapter impl : types.values()) {
            String itemId = impl.getAdapterId(block);
            if (itemId != null) id = itemId;
        }
        return id;
    }

    /**
     * Checks if a given item ID is a valid adapter ID.
     *
     * @param itemId the item ID to check, in the format "type:id" (e.g. "mc:dirt")
     * @return true if the item ID is a valid adapter ID, false otherwise
     */
    public static boolean isValidAdapterId(String itemId) {
        String type = itemId.substring(0, itemId.indexOf(":"));
        itemId = itemId.substring(itemId.indexOf(":") + 1);
        for (ImplementationAdapter impl : types.values()) {
            if (impl.existItemAdapter(itemId)) return true;
        }
        return false;
    }


    /**
     * Checks if a list of adapter IDs is valid.
     *
     * @param i the list of adapter IDs to check
     * @return true if all adapter IDs in the list are valid, false otherwise
     */
    public static boolean isValidAdapterIds(List<String> i) {
        for (String str : i) {
            if (!isValidAdapterId(str)) return false;
        }
        return true;
    }

    /**
     * Retrieves the types of implementation adapters.
     *
     * @return a HashMap containing the implementation adapter types as keys, and the corresponding ImplementationAdapter objects as values
     */
    public static HashMap<String, ImplementationAdapter> getTypes() {
        return types;
    }
}