package dev.wuason.mechanics.inventory.types.anvil.pages;

import dev.wuason.mechanics.inventory.items.def.pages.ContentItem;
import dev.wuason.mechanics.inventory.items.def.pages.PageButton;
import dev.wuason.mechanics.inventory.pages.PageContent;
import dev.wuason.mechanics.inventory.pages.SlotsData;
import dev.wuason.mechanics.inventory.events.InventoryCloseEvent;
import dev.wuason.mechanics.inventory.types.PlayerMenu;
import dev.wuason.mechanics.inventory.types.anvil.InvCustomAnvil;
import dev.wuason.mechanics.items.ItemBuilder;
import dev.wuason.mechanics.items.save.ItemSaver;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.function.TriFunction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class InvCustomAnvilPages<T> extends InvCustomAnvil implements PageContent<T>, SlotsData, PlayerMenu {

    private volatile int currentPage = 0;

    private List<T> contentList = new ArrayList<>();

    private List<Integer> slots = new ArrayList<>() {{
        addAll(IntStream.rangeClosed(9, 35).boxed().toList());
    }};

    private final Player player;
    private final ItemSaver itemSaver = new ItemSaver();
    private ItemStack contentItemDefault = new ItemStack(Material.STONE);
    private boolean autoLoadItems = true;

    private final List<Consumer<PageButton>> pageButtonListeners = new ArrayList<>();
    private final List<BiConsumer<InventoryClickEvent, ContentItem<T>>> contentClickListeners = new ArrayList<>();
    private final List<TriFunction<Integer, Integer, T, ItemStack>> contentPageListeners = new ArrayList<>();
    private final List<BiConsumer<Boolean, Boolean>> updateListeners = new ArrayList<>();

    // ############################# CONSTRUCTORS #############################

    public InvCustomAnvilPages(String title, Player player) {
        super(title, player);
        addCloseEventsListeners(this::handleClose1);
        this.player = player;
    }

    public InvCustomAnvilPages(Component title, Player player) {
        super(title, player);
        addCloseEventsListeners(this::handleClose1);
        this.player = player;
    }

    // ############################# EVENTS LISTENERS #############################

    public void onContentClick(InventoryClickEvent event, ContentItem<T> content) {
    }

    public ItemStack onContentPage(int slot, int page, T content) {
        return null;
    }

    public void onNextPage(PageButton pageButton) {
    }

    public void onPreviousPage(PageButton pageButton) {
    }

    public void onUpdate(boolean hasNext, boolean hasPrevious) {
    }

    // ############################# LISTENERS #############################

    public void addPageButtonListener(Consumer<PageButton> listener) {
        pageButtonListeners.add(listener);
    }

    public void addContentClickListener(BiConsumer<InventoryClickEvent, ContentItem<T>> listener) {
        contentClickListeners.add(listener);
    }

    public void addContentPageListener(TriFunction<Integer, Integer, T, ItemStack> listener) {
        contentPageListeners.add(listener);
    }

    public void addUpdateListener(BiConsumer<Boolean, Boolean> listener) {
        updateListeners.add(listener);
    }

    // ############################# PAGEABLE #############################

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        updatePage();
    }

    @Override
    public int getMaxPage() {
        return (int) Math.ceil((double) contentList.size() / slots.size());
    }

    @Override
    public void updatePage() {
        handleUpdate();
        setContent(currentPage);
    }

    @Override
    public int getMinPage() {
        return 0;
    }

    @Override
    public boolean nextPage() {
        if (currentPage + 1 < getMaxPage()) {
            setCurrentPage(currentPage + 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean previousPage() {
        if (currentPage - 1 >= getMinPage()) {
            setCurrentPage(currentPage - 1);
            return true;
        }
        return false;
    }

    // ############################# CONTENT #############################

    @Override
    public List<T> getContent() {
        return contentList;
    }

    @Override
    public void setContent(List<T> content) {
        Objects.requireNonNull(content, "Content cannot be null");
        contentList = content;
    }

    @Override
    public void addContent(T content) {
        contentList.add(content);
    }

    @Override
    public void removeContent(T content) {
        contentList.remove(content);
    }

    @Override
    public void clearContent() {
        contentList.clear();
    }

    @Override
    public void addAllContent(List<T> content) {
        Objects.requireNonNull(content, "Content cannot be null");
        contentList.addAll(content);
    }

    // ############################# SLOTS #############################

    @Override
    public List<Integer> getSlotsData() {
        return slots;
    }

    @Override
    public void setSlotsData(List<Integer> slots) {
        Objects.requireNonNull(slots, "Slots cannot be null");
        this.slots = slots;
    }

    @Override
    public void addSlotData(int slot) {
        slots.add(slot);
    }

    @Override
    public void removeSlotData(int slot) {
        slots.remove(slot);
    }

    @Override
    public void clearSlotsData() {
        slots.clear();
    }

    @Override
    public void addSlotsData(List<Integer> slots) {
        Objects.requireNonNull(slots, "Slots cannot be null");
        this.slots.addAll(slots);
    }

    // ############################# PRIVATE METHODS #############################

    protected final void handleClose1(InventoryCloseEvent event) {
        if (autoLoadItems) returnItems();
    }

    private void resetContent() {
        slots.forEach(s -> player.getInventory().clear(s));
    }

    private void setContent(int page) {
        resetContent();
        int start = page * slots.size();
        int end = Math.min((page + 1) * slots.size(), contentList.size());
        for (int i = start; i < end; i++) {
            T content = contentList.get(i);
            int slot = slots.get(i - start);
            ItemStack itemStack = handleContentPage(slot, page, content);
            if (itemStack.getType() == Material.AIR) {
                setPlayerItem(player, slot, null);
                return;
            }
            ContentItem<T> contentItem = new ContentItem<>(slot, itemStack, content, page);
            setPlayerItemInterface(player, contentItem);
        }
    }

    @Override
    public final void handlePreviousPage(PageButton pageButton) {
        pageButtonListeners.forEach(c -> c.accept(pageButton));
        if (previousPage()) {
            onPreviousPage(pageButton);
        }
    }

    @Override
    public final void handleNextPage(PageButton pageButton) {
        pageButtonListeners.forEach(c -> c.accept(pageButton));
        if (nextPage()) {
            onNextPage(pageButton);
        }
    }

    @Override
    public final void handleClickContent(InventoryClickEvent event, ContentItem<T> contentItem) {
        contentClickListeners.forEach(c -> c.accept(event, contentItem));
        onContentClick(event, contentItem);
    }

    @Override
    public final ItemStack handleContentPage(int slot, int page, T content) {
        ItemStack itemStack = onContentPage(slot, page, content);
        for (TriFunction<Integer, Integer, T, ItemStack> listener : contentPageListeners) {
            ItemStack contentItem = listener.apply(slot, page, content);
            if (contentItem != null) {
                itemStack = contentItem;
            }
        }
        return itemStack == null ? ItemBuilder.copyOf(contentItemDefault).setName(content.toString()).build() : itemStack;
    }

    @Override
    public final void handleUpdate() {
        boolean hasNext = hasNextPage();
        boolean hasPrevious = hasPreviousPage();
        onUpdate(hasNext, hasPrevious);
        updateListeners.forEach(c -> c.accept(hasNext, hasPrevious));
    }

    // ############################# PUBLIC METHODS #############################

    @Override
    public void open() {
        open(true);
    }

    public void open(boolean saveItems) {
        if (saveItems) saveItems();
        super.open();
    }

    @Override
    public void open(Player player) {
        open(true);
    }

    public void setRenameItem(ItemStack itemStack, boolean voidName) {
        setItem(0, voidName ? new ItemBuilder(itemStack).buildWithVoidName() : itemStack, inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        setItem(1, voidName ? new ItemBuilder(itemStack).buildWithVoidName() : itemStack, inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
    }

    public void saveItems() {
        itemSaver.save(player, player.getInventory().getContents());
        player.getInventory().clear();
    }

    public void returnItems() {
        if (!itemSaver.hasSavedItems(player)) return;
        player.getInventory().clear();
        player.getInventory().setContents(itemSaver.load(player));
    }

    public ItemSaver getItemSaver() {
        return itemSaver;
    }

    public int getPageOf(T content) {
        return (int) Math.ceil((double) contentList.indexOf(content) / slots.size());
    }

    public int getSlotOf(T content) {
        return contentList.indexOf(content) % slots.size();
    }

    public void setContentItemDefault(ItemStack contentItemDefault) {
        this.contentItemDefault = contentItemDefault;
    }

    public ItemStack getContentItemDefault() {
        return contentItemDefault;
    }

    public boolean hasNextPage() {
        return currentPage + 1 < getMaxPage();
    }

    public boolean hasPreviousPage() {
        return currentPage - 1 >= getMinPage();
    }

    public void setHotBarSelectNull() {
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getItem(i) == null) {
                player.getInventory().setHeldItemSlot(i);
            }
        }
    }

    public void setAutoLoadItems(boolean autoLoadItems) {
        this.autoLoadItems = autoLoadItems;
    }

    public boolean isAutoLoadItems() {
        return autoLoadItems;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
