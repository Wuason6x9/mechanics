package dev.wuason.mechanics.invmechanic.types;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.invmechanic.events.CloseEvent;
import dev.wuason.mechanics.invmechanic.events.ItemInterfaceClickEvent;
import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import dev.wuason.mechanics.utils.AdventureUtils;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

public class InvCustom implements InventoryHolder {
    private Inventory inventory;
    private boolean damageCancel = false;
    private boolean pickupCancel = false;
    private static final String NAMESPACED_KEY_BLOCKED = "icm_blocked";
    public static final String NAMESPACE_ITEM_INTERFACE_PREFIX = "imcp";

    private HashMap<String, ItemInterface> itemInterfaces = new HashMap<>();

    private List<Consumer<InventoryClickEvent>> clickEventsListeners = new ArrayList<>();
    private List<Consumer<InventoryOpenEvent>> openEventsListeners = new ArrayList<>();
    private List<Consumer<CloseEvent>> closeEventsListeners = new ArrayList<>();
    private List<Consumer<InventoryDragEvent>> dragEventsListeners = new ArrayList<>();
    private List<Consumer<ItemInterfaceClickEvent>> itemInterfaceClickEventsListeners = new ArrayList<>();
    private HashMap<Integer, Consumer<InventoryClickEvent>> slotClickEventsListeners = new HashMap<>();

    //*************************INVENTORY*************************

    public InvCustom(String title, int size) {
        this.inventory = Bukkit.createInventory(this, size, title);
    }
    public InvCustom(String title, InventoryType type) {
        this.inventory = Bukkit.createInventory(this, type, title);
    }
    public InvCustom(String title, int size, Function<InvCustom, Inventory> function) {
        this.inventory = function.apply(this);
        if(inventory == null){
            this.inventory = Bukkit.createInventory(this, size, title);
        }
    }
    public InvCustom(Function<InvCustom, Inventory> function) {
        this.inventory = function.apply(this);
        if(inventory == null){
            throw new IllegalArgumentException("The inventory is null!");
        }
        if(inventory.getHolder() != this){
            throw new IllegalArgumentException("The inventory holder must be the same as the inventory custom");
        }
    }
    public InvCustom(Inventory inventory) {
        this.inventory = inventory;
        if(inventory.getHolder() != this){
            throw new IllegalArgumentException("The inventory holder must be the same as the inventory custom");
        }
    }
    public InvCustom() {
    }

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

    public void onClick(InventoryClickEvent event) {
    }
    public void onOpen(InventoryOpenEvent event) {
    }
    public void onClose(CloseEvent closeEvent) {
    }
    public void onDrag(InventoryDragEvent event) {
    }
    public void onItemInterfaceClick(ItemInterfaceClickEvent event){
    }


    //*************************HANDLE EVENTS*************************


    public void handleClick(InventoryClickEvent event) {

        if(event.getCurrentItem() != null){
            if(event.getCurrentItem().getItemMeta() != null){
                //ITEM INTERFACE CLICK
                if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Mechanics.getInstance(), InvCustom.NAMESPACE_ITEM_INTERFACE_PREFIX) , PersistentDataType.STRING)){
                    String namespace = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Mechanics.getInstance(), InvCustom.NAMESPACE_ITEM_INTERFACE_PREFIX) , PersistentDataType.STRING);
                    if(itemInterfaces.containsKey(namespace)){
                        event.setCancelled(true);
                        ItemInterface itemInterface = itemInterfaces.get(namespace);
                        itemInterface.onClick(event, this);
                        ItemInterfaceClickEvent itemInterfaceClickEvent = new ItemInterfaceClickEvent(itemInterface, event);
                        itemInterfaceClickEventsListeners.forEach(consumer -> consumer.accept(itemInterfaceClickEvent));
                        onItemInterfaceClick(itemInterfaceClickEvent);
                    }
                }
            }
        }

        onClick(event);

        if(slotClickEventsListeners.containsKey(event.getRawSlot())){
            slotClickEventsListeners.get(event.getSlot()).accept(event);
        }

        clickEventsListeners.forEach(consumer -> consumer.accept(event));
    }
    public void handleOpen(InventoryOpenEvent event) {

        onOpen(event);

        openEventsListeners.forEach(consumer -> consumer.accept(event));
    }
    public void handleClose(CloseEvent closeEvent) {

        onClose(closeEvent);

        closeEventsListeners.forEach(consumer -> consumer.accept(closeEvent));
    }
    public void handleDrag(InventoryDragEvent event) {
        onDrag(event);
        dragEventsListeners.forEach(consumer -> consumer.accept(event));
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    //*************************EVENTS LISTENERS*************************
    public void addClickEventsListeners(Consumer<InventoryClickEvent> consumer){
        clickEventsListeners.add(consumer);
    }

    public void addOpenEventsListeners(Consumer<InventoryOpenEvent> consumer){
        openEventsListeners.add(consumer);
    }

    public void addCloseEventsListeners(Consumer<CloseEvent> consumer){
        closeEventsListeners.add(consumer);
    }

    public void addDragEventsListeners(Consumer<InventoryDragEvent> consumer){
        dragEventsListeners.add(consumer);
    }

    public void addItemInterfaceClickEventsListeners(Consumer<ItemInterfaceClickEvent> consumer){
        itemInterfaceClickEventsListeners.add(consumer);
    }

    public void addSlotClickEventsListeners(int slot, Consumer<InventoryClickEvent> consumer){
        slotClickEventsListeners.put(slot, consumer);
    }

    public void removeClickEventsListeners(Consumer<InventoryClickEvent> consumer){
        clickEventsListeners.remove(consumer);
    }

    public void removeOpenEventsListeners(Consumer<InventoryOpenEvent> consumer){
        openEventsListeners.remove(consumer);
    }

    public void removeCloseEventsListeners(Consumer<CloseEvent> consumer){
        closeEventsListeners.remove(consumer);
    }

    public void removeDragEventsListeners(Consumer<InventoryDragEvent> consumer){
        dragEventsListeners.remove(consumer);
    }

    public void removeItemInterfaceClickEventsListeners(Consumer<ItemInterfaceClickEvent> consumer){
        itemInterfaceClickEventsListeners.remove(consumer);
    }

    public void removeSlotClickEventsListeners(int slot){
        slotClickEventsListeners.remove(slot);
    }

    public void clearClickEventsListeners(){
        clickEventsListeners.clear();
    }

    public void clearOpenEventsListeners(){
        openEventsListeners.clear();
    }

    public void clearCloseEventsListeners(){
        closeEventsListeners.clear();
    }

    public void clearDragEventsListeners(){
        dragEventsListeners.clear();
    }

    public void clearItemInterfaceClickEventsListeners(){
        itemInterfaceClickEventsListeners.clear();
    }

    public void clearSlotClickEventsListeners(){
        slotClickEventsListeners.clear();
    }

    public void clearAllEventsListeners(){
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

    public List<Consumer<CloseEvent>> getCloseEventsListeners() {
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

    public void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> consumer){
        inventory.setItem(slot, item);
        slotClickEventsListeners.put(slot, consumer);
    }
    //ARRAY
    public void setItem(int[] slot, ItemStack item, Consumer<InventoryClickEvent> consumer){
        for(int i : slot){
            inventory.setItem(i, item);
            slotClickEventsListeners.put(i, consumer);
        }
    }
    //remove
    public void removeItem(int slot){
        inventory.setItem(slot, null);
        slotClickEventsListeners.remove(slot);
    }
    //ARRAY
    public void removeItem(int[] slot){
        for(int i : slot){
            inventory.setItem(i, null);
            slotClickEventsListeners.remove(i);
        }
    }

    //*************** WITH ITEM INTERFACE ***************

    //******* Register & Unregister ********

    public void unRegisterItemInterface(String id){
        itemInterfaces.remove(id);
    }
    public void unRegisterItemInterface(ItemInterface itemInterface){
        itemInterfaces.remove(itemInterface.getId());
    }
    public ItemInterface registerItemInterface(ItemInterface itemInterface){
        itemInterfaces.put(itemInterface.getId(), itemInterface);
        return itemInterface;
    }

    //******* Set & Remove ********

    public void setItemInterfaceInv(ItemInterface itemInterface){
        inventory.setItem(itemInterface.getSlot(), itemInterface.getItemModified());
    }
    //ARRAY
    public void setItemInterfaceInv(ItemInterface[] itemInterfaces){
        for(ItemInterface itemInterface : itemInterfaces){
            inventory.setItem(itemInterface.getSlot(), itemInterface.getItemModified());
        }
    }
    public void setItemInterfaceInv(String id){
        if(!itemInterfaces.containsKey(id)) return;
        inventory.setItem(itemInterfaces.get(id).getSlot(), itemInterfaces.get(id).getItemModified());
    }

    public void removeItemInterfaceInv(String id){
        inventory.setItem(itemInterfaces.get(id).getSlot(), null);
    }
    public void removeItemInterfaceInv(ItemInterface itemInterface){
        inventory.setItem(itemInterface.getSlot(), null);
    }

    public void removeItemInterfaceInv(ItemInterface[] itemInterfaces){
        for(ItemInterface itemInterface : itemInterfaces){
            inventory.setItem(itemInterface.getSlot(), null);
        }
    }

    //******* Register & Set ********

    public void regAndSetInvItemInterface(ItemInterface itemInterface){
        registerItemInterface(itemInterface);
        setItemInterfaceInv(itemInterface);
    }
    public void regAndSetInvItemInterface(ItemInterface[] itemInterfaces){
        for(ItemInterface itemInterface : itemInterfaces){
            regAndSetInvItemInterface(itemInterface);
        }
    }

    //******** Checks ********
    public boolean isItemInterface(int slot){
        if(inventory.getItem(slot) != null){
            ItemMeta itemMeta = inventory.getItem(slot).getItemMeta();
            return itemMeta.getPersistentDataContainer().has(new NamespacedKey(Mechanics.getInstance(), InvCustom.NAMESPACE_ITEM_INTERFACE_PREFIX), PersistentDataType.STRING);
        }
        return false;
    }
    public boolean isItemInterface(ItemStack item){
        if(item != null){
            ItemMeta itemMeta = item.getItemMeta();
            return itemMeta.getPersistentDataContainer().has(new NamespacedKey(Mechanics.getInstance(), InvCustom.NAMESPACE_ITEM_INTERFACE_PREFIX), PersistentDataType.STRING);
        }
        return false;
    }

    //****** Getters ******
    public HashMap<String, ItemInterface> getItemInterfaces() {
        return itemInterfaces;
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

    // Intercambia dos ítems en el inventario
    public void swapItems(int slot1, int slot2) {
        ItemStack item1 = inventory.getItem(slot1);
        ItemStack item2 = inventory.getItem(slot2);
        inventory.setItem(slot1, item2);
        inventory.setItem(slot2, item1);
    }

    // Desplaza todos los ítems en el inventario hacia la derecha
    public void shiftItemsRight() {
        ItemStack[] items = inventory.getContents();
        ItemStack lastItem = items[items.length - 1];
        System.arraycopy(items, 0, items, 1, items.length - 1);
        items[0] = lastItem;
        inventory.setContents(items);
    }

    // Desplaza todos los ítems en el inventario hacia la izquierda
    public void shiftItemsLeft() {
        ItemStack[] items = inventory.getContents();
        ItemStack firstItem = items[0];
        System.arraycopy(items, 1, items, 0, items.length - 1);
        items[items.length - 1] = firstItem;
        inventory.setContents(items);
    }

    // Obtiene un mapa de todos los ítems en el inventario y sus cantidades
    public Map<ItemStack, Integer> getItemCounts() {
        Map<ItemStack, Integer> itemCounts = new HashMap<>();
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                itemCounts.put(item, itemCounts.getOrDefault(item, 0) + item.getAmount());
            }
        }
        return itemCounts;
    }

    // Elimina una cantidad específica de un ítem específico
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

    // Comprueba si hay suficiente espacio para un conjunto de ítems


    // Obtiene la cantidad de un ítem específico
    public int getItemCount(ItemStack item) {
        return inventory.all(item).values().stream().mapToInt(ItemStack::getAmount).sum();
    }

    // Obtiene el ítem con la mayor cantidad
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

    // Obtiene un item de una ranura específica
    public ItemStack getItemInSlot(int slot) {
        return inventory.getItem(slot);
    }

    // Llena la fila superior con un item específico
    public void fillTopRow(ItemStack item) {
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, item);
        }
    }

    // Llena la fila inferior con un item específico
    public void fillBottomRow(ItemStack item) {
        int size = inventory.getSize();
        for (int i = size - 9; i < size; i++) {
            inventory.setItem(i, item);
        }
    }

    // Llena la columna izquierda con un item específico
    public void fillLeftColumn(ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i += 9) {
            inventory.setItem(i, item);
        }
    }

    // Llena la columna derecha con un item específico
    public void fillRightColumn(ItemStack item) {
        for (int i = 8; i < inventory.getSize(); i += 9) {
            inventory.setItem(i, item);
        }
    }

    // Obtiene todos los items de la fila superior
    public ItemStack[] getTopRowItems() {
        ItemStack[] items = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            items[i] = inventory.getItem(i);
        }
        return items;
    }

    // Obtiene todos los items de la fila inferior
    public ItemStack[] getBottomRowItems() {
        ItemStack[] items = new ItemStack[9];
        int size = inventory.getSize();
        for (int i = 0; i < 9; i++) {
            items[i] = inventory.getItem(size - 9 + i);
        }
        return items;
    }

    // Llena el inventario con un array de items, comenzando por la ranura 0
    public void fillInventoryFromArray(ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            if (i >= inventory.getSize()) {
                break; // No hay más espacio en el inventario
            }
            inventory.setItem(i, items[i]);
        }
    }

    // Obtiene todos los items del inventario en un array
    public ItemStack[] getInventoryArray() {
        return inventory.getContents();
    }

    // Obtiene todos los slots que no están vacíos
    public int[] getFilledSlots() {
        return IntStream.range(0, inventory.getSize())
                .filter(i -> inventory.getItem(i) != null && inventory.getItem(i).getType() != Material.AIR)
                .toArray();
    }

    // Remueve todos los items del inventario
    public void clearInventory() {
        inventory.clear();
    }

    // Verifica si un slot específico está vacío
    public boolean isSlotEmpty(int slot) {
        ItemStack item = inventory.getItem(slot);
        return item == null || item.getType() == Material.AIR;
    }

    // Encuentra el primer slot vacío y retorna su índice, o -1 si el inventario está lleno
    public int findFirstEmptySlot() {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (isSlotEmpty(i)) {
                return i;
            }
        }
        return -1;
    }

    // Encuentra el último slot vacío y retorna su índice, o -1 si el inventario está lleno
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
        inventory.setItem(slot, item);
    }

    public void setSimpleItems(int[] slots, ItemStack item) {
        for (int slot : slots) {
            inventory.setItem(slot, item);
        }
    }

    public void updateTitle(String title) {
        for(HumanEntity viewer : inventory.getViewers()) {
            if(viewer instanceof Player) {
                Player p = (Player) viewer;
                Mechanics.getInstance().getServerNmsVersion().getVersionWrapper().updateCurrentInventoryTitle(AdventureUtils.deserializeJson(title, p),p);
            }
        }
    }

    public void setInventoryTitle(Inventory inventory) {
        this.inventory = inventory;
    }

}
