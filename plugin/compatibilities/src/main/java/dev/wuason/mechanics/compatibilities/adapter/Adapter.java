package dev.wuason.mechanics.compatibilities.adapter;

import dev.wuason.mechanics.compatibilities.adapter.plugins.*;
import dev.wuason.mechanics.compatibilities.adapter.plugins.VanillaImpl;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Adapter {

    public static final ImplAdapter[] implementationAdapters = {
            new OraxenImpl(),
            new ItemsAdderImpl(),
            new MythicImpl(),
            new MMOItemsImpl(),
            new ExecutableItemsImpl(),
            new ExecutableBlocksImpl(),
            new CustomItemsImpl(),
            new MythicCrucibleImpl(),
            new StorageMechanicImpl(),
            new VanillaImpl()
    };

    private final static Adapter I = new Adapter();

    private Adapter() {
        for (ImplAdapter impl : implementationAdapters) {
            compatibility_adapters.put(impl.getPluginCompatibility().getPluginName().toUpperCase(Locale.ENGLISH), impl);
            types.put(impl.getType(), impl);
        }
    }

    final HashMap<String, ImplAdapter> compatibility_adapters = new HashMap<>();
    final HashMap<String, ImplAdapter> types = new HashMap<>();

    public static void registerAdapter(ImplAdapter impl) {
        I.compatibility_adapters.put(impl.getPluginCompatibility().getPluginName().toUpperCase(Locale.ENGLISH), impl);
        I.types.put(impl.getType(), impl);
    }

    public static void unregisterAdapter(ImplAdapter impl) {
        I.compatibility_adapters.remove(impl.getPluginCompatibility().getPluginName().toUpperCase(Locale.ENGLISH));
        I.types.remove(impl.getType());
    }

    public static void unregisterAdapter(String pluginName) {
        ImplAdapter impl = I.compatibility_adapters.get(pluginName.toUpperCase(Locale.ENGLISH));
        if (impl != null) {
            I.compatibility_adapters.remove(pluginName.toUpperCase(Locale.ENGLISH));
            I.types.remove(impl.getType());
        }
    }

    public static ImplAdapter getAdapter(String pluginName) {
        return I.compatibility_adapters.get(pluginName.toUpperCase(Locale.ENGLISH));
    }

    public static ImplAdapter getAdapterByType(String type) {
        return I.types.get(type);
    }

    public static boolean existAdapter(String pluginName) {
        return I.compatibility_adapters.containsKey(pluginName.toUpperCase(Locale.ENGLISH));
    }

    public static boolean existAdapterByType(String type) {
        return I.types.containsKey(type);
    }

    public static List<String> getAdapterTypes() {
        return new ArrayList<>(I.types.keySet());
    }

    public static List<String> getAdapterPlugins() {
        return new ArrayList<>(I.compatibility_adapters.keySet());
    }

    public static List<ImplAdapter> getAdapters() {
        return List.copyOf(I.types.values());
    }

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
        String[] data = I.process(itemId);
        if (data == null) return null;
        return getAdapterByType(data[0]).getAdapterItem(data[1]);
    }

    /**
     * Retrieves a list of ItemStacks based on the given item IDs.
     *
     * @param itemsId the list of item IDs, each in the format "type:id" (e.g. "mc:dirt")
     * @return a list of corresponding ItemStacks, or an empty list if no items exist
     */

    public static List<ItemStack> getItemsStack(List<String> itemsId) {
        return itemsId.stream().map(Adapter::getItemStack).filter(itemStack -> itemStack != null).toList();
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
            ItemStack i1 = getItemStack(item1);
            ItemStack i2 = getItemStack(item2);
            return i1 != null && i2 != null && i1.isSimilar(i2);
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
    public static boolean compareItems(@NotNull String adapterId, @NotNull ItemStack itemStack) {
        Objects.requireNonNull(adapterId, "adapterId cannot be null");
        Objects.requireNonNull(itemStack, "itemStack cannot be null");
        ItemStack i1 = getItemStack(adapterId);
        return i1 != null && i1.isSimilar(itemStack);
    }

    /**
     * Compares the given list of adapters with the provided ItemStack.
     * Returns true if any of the adapters is similar to the ItemStack.
     *
     * @param adapters  the list of adapter IDs to compare with
     * @param itemStack the ItemStack to compare with
     * @return adapterId if any of the adapters is similar to the ItemStack, null otherwise
     */
    public static String compareItems(@NotNull List<String> adapters, @NotNull ItemStack itemStack) {
        Objects.requireNonNull(adapters, "adapters cannot be null");
        Objects.requireNonNull(itemStack, "itemStack cannot be null");
        for (String adapter : adapters) {
            if (compareItems(adapter, itemStack)) return adapter;
        }
        return null;
    }

    //****************************************
    //************ GET ADAPTER ID ************
    //****************************************

    /**
     * Retrieves the adapter ID for the given ItemStack.
     *
     * @param i the input ItemStack
     * @return the adapter ID for the given ItemStack, or null if it cannot be computed
     */
    public static String getAdapterId(ItemStack i) { //New format mc:dirt:{nbt}
        if (i == null) return null;
        ItemStack itemStack = i.clone();
        for (ImplAdapter impl : I.types.values()) {
            String itemId = impl.getAdapterId(itemStack);
            if (itemId != null) return itemId;
        }
        return null;
    }

    /**
     * Retrieves the adapter ID for the given Block.
     *
     * @param block the Block to get the adapter ID for
     * @return the adapter ID for the given Block, or null if the Block is null
     */
    public static String getAdapterId(Block block) {
        if (block == null) return null;
        for (ImplAdapter impl : I.types.values()) {
            String itemId = impl.getAdapterId(block);
            if (itemId != null) return itemId;
        }
        return null;
    }

    /**
     * Retrieves the adapter ID for the given Entity.
     *
     * @param entity the Entity to get the adapter ID for
     * @return the adapter ID for the given Entity, or null if the Entity is null
     */

    public static String getAdapterId(Entity entity) {
        if (entity == null) return null;
        for (ImplAdapter impl : I.types.values()) {
            String itemId = impl.getAdapterId(entity);
            if (itemId != null) return itemId;
        }
        return null;
    }

    //****************************************
    //************ VALIDATIONS ***************
    //****************************************

    /**
     * Checks if a given item ID exists.
     *
     * @param id the item ID to check, in the format "type:id" (e.g. "mc:dirt")
     * @return true if the item ID exists, false otherwise
     */

    public static boolean exists(String id) {
        if (!isValid(id)) return false;
        for (ImplAdapter impl : I.types.values()) {
            if (impl.existItemAdapter(id)) return true;
        }
        return false;
    }

    /**
     * Checks if a given item ID is a valid adapter ID.
     *
     * @param itemId the item ID to check, in the format "type:id" (e.g. "mc:dirt")
     * @return true if the item ID is a valid adapter ID, false otherwise
     */
    public static boolean isValid(String itemId) {
        String[] data = I.process(itemId);
        return data != null;
    }


    /**
     * Checks if a list of adapter IDs is valid.
     *
     * @param i the list of adapter IDs to check
     * @return true if all adapter IDs in the list are valid, false otherwise
     */
    public static boolean isValidAdapterIds(List<String> i) {
        return i.stream().allMatch(Adapter::isValid);
    }

    private String[] process(String line) {
        if (line == null || line.isBlank() || !line.contains(":")) return null;
        String type = line.substring(0, line.indexOf(":")).toLowerCase(Locale.ENGLISH);
        String id = line.substring(line.indexOf(":") + 1);
        if (type.isBlank() || id.isBlank() || !types.containsKey(type)) return null;
        return new String[]{type, id};
    }
}