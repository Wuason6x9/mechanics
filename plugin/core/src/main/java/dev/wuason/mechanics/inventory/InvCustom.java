package dev.wuason.mechanics.inventory;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.inventory.events.InventoryCloseEvent;
import dev.wuason.mechanics.inventory.events.ItemInterfaceClickEvent;
import dev.wuason.mechanics.utils.TimeUnitsUtils;
import dev.wuason.mechanics.inventory.items.ItemInterface;
import dev.wuason.mechanics.utils.AdventureUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

public class InvCustom implements InventoryHolder {

    //*************************VARIABLES*************************

    //Inventory
    private Inventory inventory;

    //Events cancel
    private boolean damageCancel = false;
    private boolean pickupCancel = false;

    //Namespaced keys
    public static final NamespacedKey NAMESPACED_KEY_BLOCKED = new NamespacedKey(Mechanics.getInstance(), "icm_blocked");

    //Item interface
    private HashMap<String, ItemInterface> itemInterfaces = new HashMap<>();

    //player apply items

    private final HashMap<Integer, ItemStack> playerApplyItems = new HashMap<>();


    //Events listeners
    private List<Consumer<InventoryClickEvent>> clickEventsListeners = new ArrayList<>();
    private List<Consumer<InventoryOpenEvent>> openEventsListeners = new ArrayList<>();
    private List<Consumer<InventoryCloseEvent>> closeEventsListeners = new ArrayList<>();
    private List<Consumer<InventoryDragEvent>> dragEventsListeners = new ArrayList<>();
    private List<Consumer<ItemInterfaceClickEvent>> itemInterfaceClickEventsListeners = new ArrayList<>();
    private HashMap<Integer, Consumer<InventoryClickEvent>> slotClickEventsListeners = new HashMap<>();

    //************************CONSTRUCTORS***********************

    /**
     * Constructor
     *
     * @param title Title of the inventory
     * @param size  Size of the inventory
     * @deprecated Use {@link InvCustom#InvCustom(Component, int)} instead
     */

    @Deprecated
    public InvCustom(String title, int size) {
        this.inventory = Bukkit.createInventory(this, size, title);
    }

    /**
     * Constructor
     *
     * @param title Title of the inventory
     * @param size  Size of the inventory
     */

    public InvCustom(Component title, int size) {
        this.inventory = Bukkit.createInventory(this, size, title);
    }

    /**
     * Constructor
     *
     * @param title Title of the inventory
     * @param type  Type of the inventory
     * @deprecated Use {@link InvCustom#InvCustom(Component, InventoryType)} instead
     */

    @Deprecated
    public InvCustom(String title, InventoryType type) {
        this.inventory = Bukkit.createInventory(this, type, title);
    }

    /**
     * Constructor
     *
     * @param title Title of the inventory
     * @param type  Type of the inventory
     */

    public InvCustom(Component title, InventoryType type) {
        this.inventory = Bukkit.createInventory(this, type, title);
    }

    /**
     * Constructor
     * Creates a custom inventory with a title and size if the function returns null or the inventory holder is not the same as the custom inventory
     *
     * @param title    Title of the inventory
     * @param size     Size of the inventory
     * @param function Function to create the inventory
     * @deprecated Use {@link InvCustom#InvCustom(Component, int, Function)} instead
     */

    @Deprecated
    public InvCustom(String title, int size, Function<InvCustom, Inventory> function) {
        this.inventory = function.apply(this);
        if (inventory == null || inventory.getHolder() != this) {
            this.inventory = Bukkit.createInventory(this, size, title);
        }
    }

    /**
     * Constructor
     * Creates a custom inventory with a title and size if the function returns null or the inventory holder is not the same as the custom inventory
     *
     * @param title    Title of the inventory
     * @param size     Size of the inventory
     * @param function Function to create the inventory
     */

    public InvCustom(Component title, int size, Function<InvCustom, Inventory> function) {
        this.inventory = function.apply(this);
        if (inventory == null || inventory.getHolder() != this) {
            this.inventory = Bukkit.createInventory(this, size, title);
        }
    }

    public InvCustom(Function<InvCustom, Inventory> function) {
        this.inventory = function.apply(this);
        if (inventory == null) {
            throw new IllegalArgumentException("The inventory is null!");
        }
        if (inventory.getHolder() != this) {
            throw new IllegalArgumentException("The inventory holder must be the same as the inventory custom");
        }
    }

    /**
     * Constructor
     * Create a custom inventory with inventory
     *
     * @param inventory
     */

    public InvCustom(Inventory inventory) {
        this.inventory = inventory;
        if (inventory.getHolder() != this) {
            throw new IllegalArgumentException("The inventory holder must be the same as the inventory custom");
        }
    }

    /**
     * Constructor
     * Create instance of InvCustom
     */

    public InvCustom() {

    }

    //*************************INVENTORY*************************

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public InvCustom getHolder() {
        return this;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }


    //*************************EVENTS*************************

    /**
     * Handle the click event
     *
     * @param e InventoryClickEvent
     */

    public void onClick(InventoryClickEvent e) {}

    /**
     * Handle the open event
     *
     * @param e InventoryOpenEvent
     */

    public void onOpen(InventoryOpenEvent e) {}

    /**
     * Handle the close event
     *
     * @param e InventoryCloseEvent
     */

    public void onClose(InventoryCloseEvent e) {}

    /**
     * Handle the drag event
     *
     * @param e InventoryDragEvent
     */

    public void onDrag(InventoryDragEvent e) {}

    /**
     * Handle the item interface click event
     *
     * @param e ItemInterfaceClickEvent
     */

    public void onItemInterfaceClick(ItemInterfaceClickEvent e) {}


    //*************************HANDLE EVENTS*************************

    final void handleClick(InventoryClickEvent event) {

        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().hasItemMeta()) {
                //ITEM INTERFACE CLICK
                if (ItemInterface.isItemInterface(event.getCurrentItem())) {
                    String id = ItemInterface.getId(event.getCurrentItem());
                    if (itemInterfaces.containsKey(id)) {
                        event.setCancelled(true);
                        ItemInterface itemInterface = itemInterfaces.get(id);
                        itemInterface.onClick(event, this);
                        ItemInterfaceClickEvent itemInterfaceClickEvent = new ItemInterfaceClickEvent(itemInterface, event);
                        itemInterfaceClickEventsListeners.forEach(consumer -> consumer.accept(itemInterfaceClickEvent));
                        onItemInterfaceClick(itemInterfaceClickEvent);
                    }
                }

                //BLOCKED ITEM
                if (isBlocked(event.getCurrentItem())) {
                    event.setCancelled(true);
                }
            }
        }

        if (slotClickEventsListeners.containsKey(event.getRawSlot())) {
            slotClickEventsListeners.get(event.getSlot()).accept(event);
        }

        clickEventsListeners.forEach(consumer -> consumer.accept(event));

        onClick(event);
    }

    final void handleOpen(InventoryOpenEvent event) {
        openEventsListeners.forEach(consumer -> consumer.accept(event));

        onOpen(event);

        if (event.getPlayer() instanceof Player player) {
            applyFuturePlayerItems(player);
        }
    }

    final void handleClose(InventoryCloseEvent event) {

        closeEventsListeners.forEach(consumer -> consumer.accept(event));

        onClose(event);
    }

    final void handleDrag(InventoryDragEvent event) {

        dragEventsListeners.forEach(consumer -> consumer.accept(event));

        onDrag(event);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    //*************************EVENTS LISTENERS*************************

    public void addClickEventsListeners(Consumer<InventoryClickEvent> consumer) {
        clickEventsListeners.add(consumer);
    }

    public void addOpenEventsListeners(Consumer<InventoryOpenEvent> consumer) {
        openEventsListeners.add(consumer);
    }

    public void addCloseEventsListeners(Consumer<InventoryCloseEvent> consumer) {
        closeEventsListeners.add(consumer);
    }

    public void addDragEventsListeners(Consumer<InventoryDragEvent> consumer) {
        dragEventsListeners.add(consumer);
    }

    public void addItemInterfaceClickEventsListeners(Consumer<ItemInterfaceClickEvent> consumer) {
        itemInterfaceClickEventsListeners.add(consumer);
    }

    public void addSlotClickEventsListeners(int rawSlot, Consumer<InventoryClickEvent> consumer) {
        slotClickEventsListeners.put(rawSlot, consumer);
    }

    public void removeSlotClickEventsListeners(int slot) {
        slotClickEventsListeners.remove(slot);
    }

    public void clearClickEventsListeners() {
        clickEventsListeners.clear();
    }

    public void clearOpenEventsListeners() {
        openEventsListeners.clear();
    }

    public void clearCloseEventsListeners() {
        closeEventsListeners.clear();
    }

    public void clearDragEventsListeners() {
        dragEventsListeners.clear();
    }

    public void clearItemInterfaceClickEventsListeners() {
        itemInterfaceClickEventsListeners.clear();
    }

    public void clearSlotClickEventsListeners() {
        slotClickEventsListeners.clear();
    }

    public void clearAllEventsListeners() {
        clearClickEventsListeners();
        clearOpenEventsListeners();
        clearCloseEventsListeners();
        clearDragEventsListeners();
        clearItemInterfaceClickEventsListeners();
    }

    //get

    public List<Consumer<InventoryClickEvent>> getClickEventsListeners() {
        return clickEventsListeners;
    }

    public List<Consumer<InventoryOpenEvent>> getOpenEventsListeners() {
        return openEventsListeners;
    }

    public List<Consumer<InventoryCloseEvent>> getCloseEventsListeners() {
        return closeEventsListeners;
    }

    public List<Consumer<InventoryDragEvent>> getDragEventsListeners() {
        return dragEventsListeners;
    }

    public List<Consumer<ItemInterfaceClickEvent>> getItemInterfaceClickEventsListeners() {
        return itemInterfaceClickEventsListeners;
    }

    public HashMap<Integer, Consumer<InventoryClickEvent>> getSlotClickEventsListeners() {
        return slotClickEventsListeners;
    }

    //*******************ITEMS METHOD***********************

    //*************** WITH CONSUMER ***************

    /**
     * Set an item in a slot with a consumer
     *
     * @param slot     Slot to set the item
     * @param item     Item to set
     * @param consumer Consumer to handle the click event
     */
    public void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> consumer) {
        if ( inventory.getItem(slot) != null && ItemInterface.isItemInterface(inventory.getItem(slot))) {
            String id = ItemInterface.getId(inventory.getItem(slot));
            if (itemInterfaces.containsKey(id)) {
                unRegisterItemInterface(id);
            }
        }
        if (item == null) return;
        inventory.setItem(slot, item);
        slotClickEventsListeners.put(slot, consumer);
    }

    /**
     * Set an item in multiple slots with a consumer
     *
     * @param slots     Slots to set the item
     * @param item     Item to set
     * @param consumer Consumer to handle the click event
     */
    public void setItem(int[] slots, ItemStack item, Consumer<InventoryClickEvent> consumer) {
        if (item == null) return;
        for (int slot : slots) {
            if ( inventory.getItem(slot) != null && ItemInterface.isItemInterface(inventory.getItem(slot))) {
                String id = ItemInterface.getId(inventory.getItem(slot));
                if (itemInterfaces.containsKey(id)) {
                    unRegisterItemInterface(id);
                }
            }
            inventory.setItem(slot, item);
            slotClickEventsListeners.put(slot, consumer);
        }
    }

    /**
     * Remove an item in a slot
     *
     * @param slot Slot to remove the item
     */
    public void removeItem(int slot) {
        if ( inventory.getItem(slot) != null && ItemInterface.isItemInterface(inventory.getItem(slot))) {
            String id = ItemInterface.getId(inventory.getItem(slot));
            if (itemInterfaces.containsKey(id)) {
                unRegisterItemInterface(id);
            }
        }
        inventory.setItem(slot, null);
        slotClickEventsListeners.remove(slot);
    }

    /**
     * Remove an item in multiple slots
     *
     * @param slot Slots to remove the item
     */
    public void removeItem(int[] slot) {
        for (int i : slot) {
            inventory.setItem(i, null);
            slotClickEventsListeners.remove(i);
        }
    }

    public ItemStack addBlocked(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(NAMESPACED_KEY_BLOCKED, PersistentDataType.BYTE, (byte) 1);
            item.setItemMeta(meta);
        }
        return item;
    }

    public boolean isBlocked(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(NAMESPACED_KEY_BLOCKED, PersistentDataType.BYTE);
    }

    //*************** WITH ITEM INTERFACE ***************

    //******* Register & Unregister ********

    public void unRegisterItemInterface(String id) {
        itemInterfaces.remove(id.toUpperCase(Locale.ENGLISH));
    }

    public void unRegisterItemInterface(ItemInterface itemInterface) {
        itemInterfaces.remove(itemInterface.getId());
    }

    public ItemInterface registerItemInterface(ItemInterface itemInterface) {
        Objects.requireNonNull(itemInterface, "ItemInterface cannot be null");
        itemInterfaces.put(itemInterface.getId(), itemInterface);
        return itemInterface;
    }

    public void unRegisterAllItemInterfaces() {
        itemInterfaces.clear();
    }

    public void unRegisterAllItemInterfaces(TimeUnitsUtils.TimeUnit timeUnit, long time) {
        itemInterfaces.values().stream().filter(itemInterface -> itemInterface.getCreationTime() + TimeUnitsUtils.toMilliSeconds(time, timeUnit) < System.currentTimeMillis()).forEach(this::unRegisterItemInterface);
    }

    //******* Set & Remove ********

    public void setItemInterface(ItemInterface itemInterface) {
        if ( inventory.getItem(itemInterface.getSlot()) != null && ItemInterface.isItemInterface(inventory.getItem(itemInterface.getSlot()))) {
            String id = ItemInterface.getId(inventory.getItem(itemInterface.getSlot()));
            if (itemInterfaces.containsKey(id)) {
                unRegisterItemInterface(id);
            }
        }
        removeItemInterface(itemInterface.getId());
        inventory.setItem(itemInterface.getSlot(), itemInterface.getItemModified());
        registerItemInterface(itemInterface);
    }

    //ARRAY
    public void setItemInterface(ItemInterface[] itemInterfaces) {
        for (ItemInterface itemInterface : itemInterfaces) {
            setItemInterface(itemInterface);
        }
    }

    public void setItemInterface(ItemInterface item, int slot) {
        if ( inventory.getItem(slot) != null && ItemInterface.isItemInterface(inventory.getItem(slot))) {
            String id = ItemInterface.getId(inventory.getItem(slot));
            if (itemInterfaces.containsKey(id)) {
                unRegisterItemInterface(id);
            }
        }
        removeItemInterface(item.getId());
        inventory.setItem(slot, item.getItemModified());
        registerItemInterface(item);
    }

    //ARRAY only slots
    public void setItemInterface(ItemInterface item, int[] slots) {
        for (int slot : slots) {
            setItemInterface(item, slot);
        }
    }

    public void removeItemInterface(String id) {
        id = id.toUpperCase(Locale.ENGLISH);
        if (!itemInterfaces.containsKey(id)) return;
        inventory.setItem(itemInterfaces.get(id).getSlot(), null);
        unRegisterItemInterface(id);
    }

    public void removeItemInterface(String[] ids) {
        for (String id : ids) {
            removeItemInterface(id);
        }
    }

    public void removeItemInterface(ItemInterface itemInterface) {
        inventory.setItem(itemInterface.getSlot(), null);
        unRegisterItemInterface(itemInterface);
    }

    public void removeItemInterface(ItemInterface[] itemInterfaces) {
        for (ItemInterface itemInterface : itemInterfaces) {
            removeItemInterface(itemInterface);
        }
    }

    public void updateItemInterface(ItemInterface itemInterface) {
        if (itemInterface == null) return;
        if ( inventory.getItem(itemInterface.getSlot()) != null && ItemInterface.isItemInterface(inventory.getItem(itemInterface.getSlot()))) {
            String id = ItemInterface.getId(inventory.getItem(itemInterface.getSlot()));
            if (itemInterfaces.containsKey(id)) {
                unRegisterItemInterface(id);
            }
        }
        if (itemInterfaces.containsKey(itemInterface.getId())) {
            inventory.setItem(itemInterface.getSlot(), itemInterface.getItemModified());
        }
    }


    //******** Checks ********
    public boolean isItemInterface(int slot) {
        if (inventory.getItem(slot) != null) {
            return ItemInterface.isItemInterface(inventory.getItem(slot));
        }
        return false;
    }

    public boolean isItemInterface(ItemStack item) {
        return ItemInterface.isItemInterface(item);
    }

    public boolean existItemInterface(String id) {
        return itemInterfaces.containsKey(id.toUpperCase(Locale.ENGLISH));
    }

    //****** Getters ******
    public HashMap<String, ItemInterface> getItemInterfaces() {
        return itemInterfaces;
    }


    //*************** PLAYER APPLY ITEMS ***************

    public void applyFuturePlayerItems(Player player) {
        for (Map.Entry<Integer, ItemStack> entry : playerApplyItems.entrySet()) {
            player.getInventory().setItem(entry.getKey(), entry.getValue());
        }
    }

    public void setFuturePlayerItem(int slot, ItemStack itemStack) {
        playerApplyItems.put(slot, itemStack);
    }

    public void removeFuturePlayerItem(int slot) {
        playerApplyItems.remove(slot);
    }

    public void clearFuturePlayerItems() {
        playerApplyItems.clear();
    }

    public void setFuturePlayerItemInterface(ItemInterface... interfaces) {
        for (ItemInterface itemInterface : interfaces) {
            setFuturePlayerItem(itemInterface.getSlot(), itemInterface.getItemModified());
            registerItemInterface(itemInterface);
        }
    }

    public void setPlayerItem(Player player, int slot, ItemStack itemStack) {
        if ( inventory.getItem(slot) != null && ItemInterface.isItemInterface(inventory.getItem(slot))) {
            String id = ItemInterface.getId(inventory.getItem(slot));
            if (itemInterfaces.containsKey(id)) {
                unRegisterItemInterface(id);
            }
        }
        player.getInventory().setItem(slot, itemStack);
    }

    public void setPlayerItemInterface(Player player, ItemInterface itemInterface) {
        Inventory inventory = player.getInventory();
        if ( inventory.getItem(itemInterface.getSlot()) != null && ItemInterface.isItemInterface(inventory.getItem(itemInterface.getSlot()))) {
            String id = ItemInterface.getId(inventory.getItem(itemInterface.getSlot()));
            if (itemInterfaces.containsKey(id)) {
                unRegisterItemInterface(id);
            }
        }
        setPlayerItem(player, itemInterface.getSlot(), itemInterface.getItemModified());
        registerItemInterface(itemInterface);
    }

    public void setPlayerItemsInterface(Player player, ItemInterface... interfaces) {
        for (ItemInterface itemInterface : interfaces) {
            setPlayerItemInterface(player, itemInterface);
        }
    }


    //*******************EVENTS CANCEL***********************
    public boolean isDamageCancel() {
        return damageCancel;
    }

    public void setDamageCancel(boolean damageCancel) {
        this.damageCancel = damageCancel;
    }

    public boolean isPickupCancel() {
        return pickupCancel;
    }

    public void setPickupCancel(boolean pickupCancel) {
        this.pickupCancel = pickupCancel;
    }


    //****************custom methods***********************

    public int[] getBorders() {
        int size = this.inventory.getSize();
        return IntStream.range(0, size).filter(i -> size < 27 || i < 9
                || i % 9 == 0 || (i - 8) % 9 == 0 || i > size - 9).toArray();
    }

    public int[] getCorners() {
        int size = this.inventory.getSize();
        return IntStream.range(0, size).filter(i -> i < 2 || (i > 6 && i < 10)
                || i == 17 || i == size - 18
                || (i > size - 11 && i < size - 7) || i > size - 3).toArray();
    }

    public void fillEmptySlots(ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, item);
            }
        }
    }

    public void swapItems(int slot1, int slot2) {
        ItemStack item1 = inventory.getItem(slot1);
        ItemStack item2 = inventory.getItem(slot2);
        inventory.setItem(slot1, item2);
        inventory.setItem(slot2, item1);
    }

    public void shiftItemsRight() {
        ItemStack[] items = inventory.getContents();
        ItemStack lastItem = items[items.length - 1];
        System.arraycopy(items, 0, items, 1, items.length - 1);
        items[0] = lastItem;
        inventory.setContents(items);
    }


    public void shiftItemsLeft() {
        ItemStack[] items = inventory.getContents();
        ItemStack firstItem = items[0];
        System.arraycopy(items, 1, items, 0, items.length - 1);
        items[items.length - 1] = firstItem;
        inventory.setContents(items);
    }

    public Map<ItemStack, Integer> getItemCounts() {
        Map<ItemStack, Integer> itemCounts = new HashMap<>();
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                itemCounts.put(item, itemCounts.getOrDefault(item, 0) + item.getAmount());
            }
        }
        return itemCounts;
    }

    public void removeItemAmount(ItemStack item, int amount) {
        int remaining = amount;
        for (ItemStack slotItem : inventory.getContents()) {
            if (slotItem != null && slotItem.isSimilar(item)) {
                int newAmount = slotItem.getAmount() - remaining;
                if (newAmount > 0) {
                    slotItem.setAmount(newAmount);
                    break;
                } else {
                    inventory.remove(slotItem);
                    remaining -= slotItem.getAmount();
                    if (remaining <= 0) break;
                }
            }
        }
    }



    public int getItemCount(ItemStack item) {
        return inventory.all(item).values().stream().mapToInt(ItemStack::getAmount).sum();
    }

    public ItemStack getMostFrequentItem() {
        Map<ItemStack, Integer> itemCounts = getItemCounts();
        return itemCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public void setItemInSlot(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    public ItemStack getItemInSlot(int slot) {
        return inventory.getItem(slot);
    }

    public void fillTopRow(ItemStack item) {
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, item);
        }
    }

    public void fillBottomRow(ItemStack item) {
        int size = inventory.getSize();
        for (int i = size - 9; i < size; i++) {
            inventory.setItem(i, item);
        }
    }

    public void fillLeftColumn(ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i += 9) {
            inventory.setItem(i, item);
        }
    }

    public void fillRightColumn(ItemStack item) {
        for (int i = 8; i < inventory.getSize(); i += 9) {
            inventory.setItem(i, item);
        }
    }

    public ItemStack[] getTopRowItems() {
        ItemStack[] items = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            items[i] = inventory.getItem(i);
        }
        return items;
    }

    public ItemStack[] getBottomRowItems() {
        ItemStack[] items = new ItemStack[9];
        int size = inventory.getSize();
        for (int i = 0; i < 9; i++) {
            items[i] = inventory.getItem(size - 9 + i);
        }
        return items;
    }

    public void fillInventoryFromArray(ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            if (i >= inventory.getSize()) {
                break; // No hay más espacio en el inventario
            }
            inventory.setItem(i, items[i]);
        }
    }

    public ItemStack[] getInventoryArray() {
        return inventory.getContents();
    }

    public int[] getFilledSlots() {
        return IntStream.range(0, inventory.getSize())
                .filter(i -> inventory.getItem(i) != null && inventory.getItem(i).getType() != Material.AIR)
                .toArray();
    }

    public void clearInventory() {
        inventory.clear();
    }

    public boolean isSlotEmpty(int slot) {
        ItemStack item = inventory.getItem(slot);
        return item == null || item.getType() == Material.AIR;
    }

    public int findFirstEmptySlot() {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (isSlotEmpty(i)) {
                return i;
            }
        }
        return -1;
    }

    public int findLastEmptySlot() {
        for (int i = inventory.getSize() - 1; i >= 0; i--) {
            if (isSlotEmpty(i)) {
                return i;
            }
        }
        return -1;
    }

    public void setItems(int[] slots, ItemStack item) {
        for (int slot : slots) {
            inventory.setItem(slot, item);
        }
    }

    public int[] getSlotsWithItem(ItemStack item) {
        return IntStream.range(0, inventory.getSize())
                .filter(i -> inventory.getItem(i) != null && inventory.getItem(i).isSimilar(item))
                .toArray();
    }

    public int[] getSlotsFree() {
        return IntStream.range(0, inventory.getSize())
                .filter(i -> inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR)
                .toArray();
    }

    public int[] getSlotsWithItem(ItemStack item, int amount) {
        return IntStream.range(0, inventory.getSize())
                .filter(i -> inventory.getItem(i) != null && inventory.getItem(i).isSimilar(item) && inventory.getItem(i).getAmount() >= amount)
                .toArray();
    }

    public int[] getSlotsWithItem(ItemStack item, int minAmount, int maxAmount) {
        return IntStream.range(0, inventory.getSize())
                .filter(i -> inventory.getItem(i) != null && inventory.getItem(i).isSimilar(item) && inventory.getItem(i).getAmount() >= minAmount && inventory.getItem(i).getAmount() <= maxAmount)
                .toArray();
    }

    public int[] getSlotsWithItem(ItemStack item, int minAmount, int maxAmount, int[] slots) {
        return IntStream.of(slots)
                .filter(i -> inventory.getItem(i) != null && inventory.getItem(i).isSimilar(item) && inventory.getItem(i).getAmount() >= minAmount && inventory.getItem(i).getAmount() <= maxAmount)
                .toArray();
    }

    public int[] getSlotsWithItem(ItemStack item, int[] slots) {
        return IntStream.of(slots)
                .filter(i -> inventory.getItem(i) != null && inventory.getItem(i).isSimilar(item))
                .toArray();
    }

    public int[] getSlotsWithItem(ItemStack item, int amount, int[] slots) {
        return IntStream.of(slots)
                .filter(i -> inventory.getItem(i) != null && inventory.getItem(i).isSimilar(item) && inventory.getItem(i).getAmount() >= amount)
                .toArray();
    }

    public int[] getSlotsWithItem(ItemStack item, int minAmount, int maxAmount, int[] slots, boolean ignoreEmpty) {
        return IntStream.of(slots)
                .filter(i -> inventory.getItem(i) != null && inventory.getItem(i).isSimilar(item) && inventory.getItem(i).getAmount() >= minAmount && inventory.getItem(i).getAmount() <= maxAmount)
                .toArray();
    }

    public int[] getSlotsWithItem(ItemStack item, int[] slots, boolean ignoreEmpty) {
        return IntStream.of(slots)
                .filter(i -> inventory.getItem(i) != null && inventory.getItem(i).isSimilar(item))
                .toArray();
    }

    //others

    public void setItem(int slot, ItemStack item) {
        if ( inventory.getItem(slot) != null && ItemInterface.isItemInterface(inventory.getItem(slot))) {
            String id = ItemInterface.getId(inventory.getItem(slot));
            if (itemInterfaces.containsKey(id)) {
                unRegisterItemInterface(id);
            }
        }
        inventory.setItem(slot, item);
    }

    public void setSimpleItems(int[] slots, ItemStack item) {
        for (int slot : slots) {
            setItem(slot, item);
        }
    }

    public void updateTitle(String title) {
        for (HumanEntity viewer : inventory.getViewers()) {
            if (viewer instanceof Player) {
                Player p = (Player) viewer;
                Mechanics.getInstance().getServerNmsVersion().getVersionWrapper().updateCurrentInventoryTitle(AdventureUtils.deserializeJson(title, p), p);
            }
        }
    }

    public static class Builder {

        private Consumer<InventoryClickEvent> onClick;
        private Consumer<InventoryOpenEvent> onOpen;
        private Consumer<InventoryCloseEvent> onClose;
        private Consumer<InventoryDragEvent> onDrag;
        private Consumer<ItemInterfaceClickEvent> onItemInterfaceClick;
        private List<Consumer<InvCustom>> invCustoms = new ArrayList<>();
        private Function<InvCustom, Inventory> inventory;
        private String title;
        private int size;
        private InventoryType type;

        private boolean damageCancel = false;
        private boolean pickupCancel = false;

        public Builder onClick(Consumer<InventoryClickEvent> onClick) {
            this.onClick = onClick;
            return this;
        }

        public Builder onOpen(Consumer<InventoryOpenEvent> onOpen) {
            this.onOpen = onOpen;
            return this;
        }

        public Builder onClose(Consumer<InventoryCloseEvent> onClose) {
            this.onClose = onClose;
            return this;
        }

        public Builder onDrag(Consumer<InventoryDragEvent> onDrag) {
            this.onDrag = onDrag;
            return this;
        }

        public Builder onItemInterfaceClick(Consumer<ItemInterfaceClickEvent> onItemInterfaceClick) {
            this.onItemInterfaceClick = onItemInterfaceClick;
            return this;
        }

        public Builder addEdit(Consumer<InvCustom> invCustom) {
            this.invCustoms.add(invCustom);
            return this;
        }

        public Builder damageCancel(boolean damageCancel) {
            this.damageCancel = damageCancel;
            return this;
        }

        public Builder pickupCancel(boolean pickupCancel) {
            this.pickupCancel = pickupCancel;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder type(InventoryType type) {
            this.type = type;
            return this;
        }

        public Builder inventory(Function<InvCustom, Inventory> inventory) {
            this.inventory = inventory;
            return this;
        }

        public InvCustom build() {
            InvCustom invCustom = new InvCustom((Function<InvCustom, Inventory>) invCustom1 -> {
                if (title != null && size != 0) return Bukkit.createInventory(invCustom1, size, title);
                if (title != null && type != null) return Bukkit.createInventory(invCustom1, type, title);
                if (inventory != null) return inventory.apply(invCustom1);
                return null;
            }) {
                @Override
                public void onClick(InventoryClickEvent event) {
                    if (onClick != null) onClick.accept(event);
                }

                @Override
                public void onOpen(InventoryOpenEvent event) {
                    if (onOpen != null) onOpen.accept(event);
                }

                @Override
                public void onClose(InventoryCloseEvent closeEvent) {
                    if (onClose != null) onClose.accept(closeEvent);
                }

                @Override
                public void onDrag(InventoryDragEvent event) {
                    if (onDrag != null) onDrag.accept(event);
                }

                @Override
                public void onItemInterfaceClick(ItemInterfaceClickEvent event) {
                    if (onItemInterfaceClick != null) onItemInterfaceClick.accept(event);
                }
            };

            invCustoms.forEach(consumer -> consumer.accept(invCustom));

            invCustom.setDamageCancel(damageCancel);
            invCustom.setPickupCancel(pickupCancel);

            return invCustom;
        }

    }
}
