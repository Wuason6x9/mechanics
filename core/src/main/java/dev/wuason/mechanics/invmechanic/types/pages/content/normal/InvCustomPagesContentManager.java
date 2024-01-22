package dev.wuason.mechanics.invmechanic.types.pages.content.normal;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.invmechanic.events.CloseEvent;
import dev.wuason.mechanics.invmechanic.types.pages.content.normal.events.ContentClickEvent;
import dev.wuason.mechanics.invmechanic.types.pages.content.normal.events.NextPageEvent;
import dev.wuason.mechanics.invmechanic.types.pages.content.normal.events.OpenPageEvent;
import dev.wuason.mechanics.invmechanic.types.pages.content.normal.events.PreviousPageEvent;
import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import dev.wuason.mechanics.invmechanic.types.pages.content.normal.items.NextPageItem;
import dev.wuason.mechanics.invmechanic.types.pages.content.normal.items.PreviousPageItem;
import dev.wuason.mechanics.invmechanic.types.InvCustom;
import dev.wuason.mechanics.items.ItemBuilderMechanic;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class InvCustomPagesContentManager<T> {
    private HashMap<Integer, InvCustomPagesContent> pages = new HashMap<>();
    private List<T> contentList = new ArrayList<>();
    private List<Integer> dataSlots = new ArrayList<>();
    private ItemInterface itemBack;
    private ItemInterface itemNext;
    public final static String NAMESPACED_CONTENT_KEY = "icm_content";
    private BiFunction<InvCustomPagesContentManager<T>,Integer, InvCustomPagesContent> defaultInventory = null;
    private List<Consumer<InvCustomPagesContent>> inventoryCustomPagesListenerCreate = new ArrayList<>();
    private List<Consumer<NextPageEvent>> nextPageListeners = new ArrayList<>();
    private List<Consumer<PreviousPageEvent>> previousPageListeners = new ArrayList<>();
    private List<Consumer<ContentClickEvent>> contentClickListeners = new ArrayList<>();
    private List<Consumer<OpenPageEvent>> openPageListeners = new ArrayList<>();

    public InvCustomPagesContentManager(List<Integer> dataSlots, PreviousPageItem itemBack, NextPageItem itemNext, BiFunction<InvCustomPagesContentManager<T>, Integer, InvCustomPagesContent> defaultInventory, List<T> contentList) {
        this(dataSlots, itemBack, itemNext);
        setContentList(contentList);
        setDefaultInventory(defaultInventory);
    }
    public InvCustomPagesContentManager(List<Integer> dataSlots, PreviousPageItem itemBack, NextPageItem itemNext) {
        this.dataSlots = dataSlots;
        this.itemBack = itemBack;
        this.itemNext = itemNext;
        addInventoryCustomPagesListenerCreate(inv -> {
            inv.registerItemInterface(itemBack);
            inv.registerItemInterface(itemNext);
            inv.addClickEventsListeners(this::handleClick);
            inv.addCloseEventsListeners(this::handleClose);
        });
    }


    //******************** EVENTS ********************
    public ItemStack onContentPage(int page, int slot, T content){
        return null;
    }
    public void onNextPage(NextPageEvent event) {
    }
    public void onPreviousPage(PreviousPageEvent event){
    }
    public void onContentClick(ContentClickEvent event){
    }
    public InvCustomPagesContent onPageCreate(InvCustomPagesContentManager inventoryCustomPagesContentManager, int page){
        return null;
    }
    public void onOpenPage(OpenPageEvent event){
    }

    //******************** LISTENERS ********************
    //add
    public void addInventoryCustomPagesListenerCreate(Consumer<InvCustomPagesContent> consumer){
        inventoryCustomPagesListenerCreate.add(consumer);
    }
    public void addNextPageListener(Consumer<NextPageEvent> consumer){
        nextPageListeners.add(consumer);
    }
    public void addPreviousPageListener(Consumer<PreviousPageEvent> consumer){
        previousPageListeners.add(consumer);
    }
    public void addContentClickListener(Consumer<ContentClickEvent> consumer){
        contentClickListeners.add(consumer);
    }
    public void addOpenPageListener(Consumer<OpenPageEvent> consumer){
        openPageListeners.add(consumer);
    }

    //remove
    public void removeInventoryCustomPagesListenerCreate(Consumer<InvCustomPagesContent> consumer){
        inventoryCustomPagesListenerCreate.remove(consumer);
    }
    public void removeNextPageListener(Consumer<NextPageEvent> consumer){
        nextPageListeners.remove(consumer);
    }
    public void removePreviousPageListener(Consumer<PreviousPageEvent> consumer){
        previousPageListeners.remove(consumer);
    }
    public void removeContentClickListener(Consumer<ContentClickEvent> consumer){
        contentClickListeners.remove(consumer);
    }
    public void removeOpenPageListener(Consumer<OpenPageEvent> consumer){
        openPageListeners.remove(consumer);
    }


    //clear
    public void clearInventoryCustomPagesListenerCreate(){
        inventoryCustomPagesListenerCreate.clear();
    }
    public void clearNextPageListener(){
        nextPageListeners.clear();
    }
    public void clearPreviousPageListener(){
        previousPageListeners.clear();
    }
    public void clearContentClickListener(){
        contentClickListeners.clear();
    }
    public void clearOpenPageListener(){
        openPageListeners.clear();
    }

    //get

    public List<Consumer<InvCustomPagesContent>> getInventoryCustomPagesListenerCreate() {
        return inventoryCustomPagesListenerCreate;
    }
    public List<Consumer<NextPageEvent>> getNextPageListeners() {
        return nextPageListeners;
    }
    public List<Consumer<PreviousPageEvent>> getPreviousPageListeners() {
        return previousPageListeners;
    }
    public List<Consumer<ContentClickEvent>> getContentClickListeners() {
        return contentClickListeners;
    }
    public List<Consumer<OpenPageEvent>> getOpenPageListeners() {
        return openPageListeners;
    }


    //******************** CONTENT ********************

    public List<T> getContentList() {
        return contentList;
    }

    public void setContentList(List<T> contentList) {
        this.contentList = new ArrayList<>(contentList);
    }

    public void addContent(T content){
        this.contentList.add(content);
    }

    public void removeContent(T content){
        this.contentList.remove(content);
    }

    public void removeContent(int index){
        this.contentList.remove(index);
    }

    public void clearContent(int page){
        if(!pages.containsKey(page)) return;
        InvCustom inventoryCustom = pages.get(page);
        for(Map.Entry<Integer, T> entry : getContentPage(page).entrySet()){
            inventoryCustom.getInventory().clear(entry.getKey());
        }
    }

    //******************** DATA SLOTS ********************

    public List<Integer> getDataSlots() {
        return dataSlots;
    }

    public void setDataSlots(List<Integer> dataSlots) {
        this.dataSlots = dataSlots;
    }

    public void addDataSlot(int dataSlot){
        this.dataSlots.add(dataSlot);
    }

    public void removeDataSlot(int dataSlot){
        this.dataSlots.remove(dataSlot);
    }

    public void clearDataSlots(int page){
        if(!pages.containsKey(page)) return;
        InvCustom inventoryCustom = pages.get(page);
        for(int dataSlot : dataSlots){
            inventoryCustom.getInventory().clear(dataSlot);
        }
    }

    //******************** PAGES ********************
    //nextPage
    public int nextPage(int page) {
        int a = page * dataSlots.size();
        if(a < contentList.size()){
            return page + 1;
        }
        return -1;
    }
    //previousPage
    public int previousPage(int page) {
        if(page > 0){
            return page - 1;
        }

        return -1;
    }

    public int getMaxPage() {
        int a = contentList.size() / dataSlots.size();
        if(contentList.size() % dataSlots.size() != 0){
            a++;
        }
        return a - 1 == -1 ? 0 : a - 1;
    }

    public int getMinPage() {
        return 0;
    }

    public boolean isPossibleNextPage(int page){
        int a = (page * dataSlots.size()) + dataSlots.size();
        return a < contentList.size();
    }

    public boolean isPossiblePreviousPage(int page){
        return page > 0;
    }

    public InvCustomPagesContent createPage(int page){
        InvCustomPagesContent inventoryCustomPagesContent = onPageCreate(this, page);
        if(inventoryCustomPagesContent == null){
            inventoryCustomPagesContent = defaultInventory.apply(this, page);
        }
        if(inventoryCustomPagesContent == null) throw new NullPointerException("InventoryCustomPagesContent is null");
        inventoryCustomPagesContent.setPage(page);
        pages.put(page, inventoryCustomPagesContent);
        for(Consumer<InvCustomPagesContent> consumer : inventoryCustomPagesListenerCreate) consumer.accept(inventoryCustomPagesContent);
        return inventoryCustomPagesContent;
    }


    /**
     * Opens a custom inventory page for a player.
     *
     * @param player the player to open the inventory for
     * @param page   the page number of the inventory
     */
    //******************** OPEN ********************
    public void open(Player player, int page){
        if(!pages.containsKey(page)){
            createPage(page);
            setContent(page);
            setButtonsPage(page);
        }
        InvCustomPagesContent inventoryCustomPagesContent = pages.get(page);
        OpenPageEvent openPageEvent = new OpenPageEvent(player, inventoryCustomPagesContent);
        onOpenPage(openPageEvent);
        openPageListeners.forEach(e -> e.accept(openPageEvent));
        if(openPageEvent.isCancelled()) return;
        inventoryCustomPagesContent.open(player);
    }

    /**
     * Opens a simple custom page for the given player with the specified page number.
     * If the page number does not exist, a new page will be created.
     * Fires the OpenPageEvent and invokes all registered open page listeners.
     * If the event is cancelled, the method will return without opening the page.
     *
     * @param player the player to open the page for
     * @param page   the page number to open
     */
    public void openSimple(Player player, int page){
        if(!pages.containsKey(page)){
            createPage(page);
        }
        InvCustomPagesContent inventoryCustomPagesContent = pages.get(page);
        OpenPageEvent openPageEvent = new OpenPageEvent(player, inventoryCustomPagesContent);
        onOpenPage(openPageEvent);
        openPageListeners.forEach(e -> e.accept(openPageEvent));
        if(openPageEvent.isCancelled()) return;
        inventoryCustomPagesContent.open(player);
    }

    //******************** DEFAULT INVENTORY ********************
    public void setDefaultInventory(BiFunction<InvCustomPagesContentManager<T>,Integer, InvCustomPagesContent> function){
        this.defaultInventory = function;
    }

    //******************** CONTENT PAGE ********************


    /**
     * Returns a HashMap containing the content for a specific page.
     *
     * @param page the page number
     * @return a HashMap with the content of the specified page
     */
    public HashMap<Integer, T> getContentPage(int page){
        HashMap<Integer, T> contentPage = new HashMap<>();
        int a = page * dataSlots.size();
        for (int i : dataSlots) {
            if(a >= contentList.size()){
                break;
            }
            contentPage.put(i, contentList.get(a));
            a++;
        }
        return contentPage;
    }

    /**
     * Sets the content of the specified page in the custom inventory.
     *
     * @param page the page number to set the content for
     */
    public void setContent(int page){
        clearDataSlots(page);
        InvCustom inventoryCustom = pages.get(page);
        for(Map.Entry<Integer, T> entry : getContentPage(page).entrySet()){
            ItemStack itemStack = onContentPage(page, entry.getKey(), entry.getValue());
            if(itemStack == null) itemStack = new ItemBuilderMechanic(Material.BOOK).setName(entry.getValue().toString()).build();
            ItemMeta itemMeta = itemStack.getItemMeta();
            String data = contentList.indexOf(entry.getValue()) + ":" + entry.getKey() + ":" + page;
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(Mechanics.getInstance(), NAMESPACED_CONTENT_KEY), PersistentDataType.STRING, data);
            itemStack.setItemMeta(itemMeta);
            inventoryCustom.getInventory().setItem(entry.getKey(), itemStack);
        }
    }


    //******************** BUTTONS ********************

    public void setButtonsPage(int page) {
        if(getItemBack() == null || getItemNext() == null || !pages.containsKey(page)) return;
        if(page == getMinPage() ){
            pages.get(page).getInventory().setItem(getItemBack().getSlot(), null);
        }
        if( page == getMaxPage() ){
            pages.get(page).getInventory().setItem(getItemNext().getSlot(), null);
        }

        if(page > getMinPage() ) {
            pages.get(page).setItemInterfaceInv(getItemBack());
        }
        if(page < getMaxPage() ) {
            pages.get(page).setItemInterfaceInv(getItemNext());
        }
    }
    public void removeButtonsPage(int page) {
        if(getItemBack() == null || getItemNext() == null || !pages.containsKey(page)) return;
        pages.get(page).getInventory().setItem(getItemBack().getSlot(), null);
        pages.get(page).getInventory().setItem(getItemNext().getSlot(), null);
    }

    public void setButtons(ItemInterface itemBack, ItemInterface itemNext){
        this.itemBack = itemBack;
        this.itemNext = itemNext;
    }

    public void setNextButton(ItemInterface itemNext){
        this.itemNext = itemNext;
    }

    public void setBackButton(ItemInterface itemBack){
        this.itemBack = itemBack;
    }

    public ItemInterface getItemBack() {
        return itemBack;
    }

    public ItemInterface getItemNext() {
        return itemNext;
    }

    //******************** HANDLERS ********************
    public void handleClose(CloseEvent event){
        InvCustomPagesContent inventoryCustomPagesContent = (InvCustomPagesContent) event.getEvent().getInventory().getHolder();
        if(inventoryCustomPagesContent.getInventory().getViewers().size() == 1){
            pages.remove(inventoryCustomPagesContent.getPage());

        }
    }
    public void handleClick(InventoryClickEvent event){
        //onContentClick
        if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;
        if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Mechanics.getInstance(), NAMESPACED_CONTENT_KEY), PersistentDataType.STRING)){
            String data = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Mechanics.getInstance(), NAMESPACED_CONTENT_KEY), PersistentDataType.STRING);
            String[] args = data.split(":");
            int slot = Integer.parseInt(args[1]);
            int page = Integer.parseInt(args[2]);
            T content = contentList.get(Integer.parseInt(args[0]));
            event.setCancelled(true);
            ContentClickEvent contentClickEvent = new ContentClickEvent(pages.get(page), event, content);
            onContentClick(contentClickEvent);
            for(Consumer<ContentClickEvent> consumer : contentClickListeners) consumer.accept(contentClickEvent);
        }
    }

}
