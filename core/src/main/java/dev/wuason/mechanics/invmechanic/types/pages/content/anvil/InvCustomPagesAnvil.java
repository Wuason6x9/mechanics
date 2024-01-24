package dev.wuason.mechanics.invmechanic.types.pages.content.anvil;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.invmechanic.events.CloseEvent;
import dev.wuason.mechanics.invmechanic.items.ItemInterface;
import dev.wuason.mechanics.invmechanic.types.InvCustomAnvil;
import dev.wuason.mechanics.invmechanic.types.pages.content.anvil.events.ContentClickAnvilEvent;
import dev.wuason.mechanics.invmechanic.types.pages.content.anvil.events.NextPageEvent;
import dev.wuason.mechanics.invmechanic.types.pages.content.anvil.events.PreviousPageEvent;
import dev.wuason.mechanics.invmechanic.types.pages.content.anvil.items.NextPageItem;
import dev.wuason.mechanics.invmechanic.types.pages.content.anvil.items.PreviousPageItem;
import dev.wuason.mechanics.items.ItemBuilderMechanic;
import org.bukkit.Bukkit;
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
import java.util.function.Consumer;

public class InvCustomPagesAnvil<T> extends InvCustomAnvil {
    private Player player;
    private ItemStack[] playerInventory = null;
    private List<Integer> dataSlots;
    private List<T> contentList;
    private List<T> searchList = new ArrayList<>();
    private ItemInterface itemNextPage;
    private ItemInterface itemPreviousPage;
    private ItemStack itemStackRename;
    private int page = 0;
    public final static String NAMESPACED_CONTENT_KEY = "icm_content";

    //listeners
    private List<Consumer<ContentClickAnvilEvent<T>>> contentClickListeners = new ArrayList<>();
    private List<Consumer<NextPageEvent>> nextPageListeners = new ArrayList<>();
    private List<Consumer<PreviousPageEvent>> previousPageListeners = new ArrayList<>();

    public InvCustomPagesAnvil(String title, Player player, List<Integer> slots, List<T> contentList, PreviousPageItem previousPageItem, NextPageItem nextPageItem, ItemStack itemStackRename) { //data slots are the slots player
        super(title, player);
        this.player = player;
        this.dataSlots = slots;
        this.contentList = contentList;
        this.itemNextPage = nextPageItem;
        this.itemPreviousPage = previousPageItem;
        if(itemStackRename != null) this.itemStackRename = new ItemBuilderMechanic(itemStackRename).buildWithVoidName();
        setMenuAnvilOptions();
        setItem(0, this.itemStackRename, event -> {
            event.setCancelled(true);
        });
        setItem(1, this.itemStackRename, event -> {
            event.setCancelled(true);
        });
        registerItemInterface(previousPageItem);
        registerItemInterface(nextPageItem);
        addCloseEventsListeners(this::handleClose2);
        addClickEventsListeners(this::handleClick1);
    }

    //************ LISTENERS ************

    public void addContentClickListeners(Consumer<ContentClickAnvilEvent<T>> contentClickAnvilEventConsumer){
        contentClickListeners.add(contentClickAnvilEventConsumer);
    }

    public void addNextPageListeners(Consumer<NextPageEvent> nextPageEventConsumer){
        nextPageListeners.add(nextPageEventConsumer);
    }

    public void addPreviousPageListeners(Consumer<PreviousPageEvent> previousPageEventConsumer){
        previousPageListeners.add(previousPageEventConsumer);
    }

    public void removeContentClickListeners(Consumer<ContentClickAnvilEvent<T>> contentClickAnvilEventConsumer){
        contentClickListeners.remove(contentClickAnvilEventConsumer);
    }

    public void removeNextPageListeners(Consumer<NextPageEvent> nextPageEventConsumer){
        nextPageListeners.remove(nextPageEventConsumer);
    }

    public void removePreviousPageListeners(Consumer<PreviousPageEvent> previousPageEventConsumer){
        previousPageListeners.remove(previousPageEventConsumer);
    }

    public void clearContentClickListeners(){
        contentClickListeners.clear();
    }

    public void clearNextPageListeners(){
        nextPageListeners.clear();
    }

    public void clearPreviousPageListeners(){
        previousPageListeners.clear();
    }

    public List<Consumer<ContentClickAnvilEvent<T>>> getContentClickListeners(){
        return contentClickListeners;
    }

    public List<Consumer<NextPageEvent>> getNextPageListeners(){
        return nextPageListeners;
    }

    public List<Consumer<PreviousPageEvent>> getPreviousPageListeners(){
        return previousPageListeners;
    }


    //************ EVENTS ************

    public void onContentClick(ContentClickAnvilEvent<T> event){

    }
    public ItemStack onContentPage(int page, int slot, T content){
        return null;
    }
    public void onNextPage(NextPageEvent event){
    }
    public void onPreviousPage(PreviousPageEvent event){
    }
    public T onContentSearch(String search, T content){
        return content;
    }

    //********** SEARCH **********

    public void setSearchList(List<T> searchList) {
        this.searchList = searchList;
    }

    public void removeSearchList(T content){
        searchList.remove(content);
    }

    public void addSearchList(T content){
        searchList.add(content);
    }

    public void clearSearchList(){
        searchList.clear();
    }

    public void search(){
        searchList.clear();
        String search = getRenameText();
        for(T content : contentList){
            T c = onContentSearch(search, content);
            if(c == null) continue;
            searchList.add(c);
        }
    }
    public void search(String search){
        searchList.clear();
        for(T content : contentList){
            T c = onContentSearch(search, content);
            if(c == null) continue;
            searchList.add(c);
        }
    }
    public void searchAsync(){
        Bukkit.getScheduler().runTaskAsynchronously(Mechanics.getInstance(), () -> {
            search();
        });
    }
    public void searchAsync(String search){
        Bukkit.getScheduler().runTaskAsynchronously(Mechanics.getInstance(), () -> {
            search(search);
        });
    }

    public List<T> getSearchList() {
        return searchList;
    }

    //********** OPEN **********

    @Override
    public void open(){
        savePlayerInventory();
        super.open();
    }

    public void openSimple(){
        super.open();
    }

    //********** INVENTORY **********

    public void savePlayerInventory(){
        playerInventory = player.getInventory().getContents();
        player.getInventory().clear();
    }

    public void restorePlayerInventory(){
        if(playerInventory == null) return;
        player.getInventory().clear();
        player.getInventory().setContents(playerInventory);
    }

    //********** HANDLERS **********
    public void handleClose2(CloseEvent closeEvent) {
        if(closeEvent.isCancelled()) return;
        restorePlayerInventory();
    }



    public void handleClick1(InventoryClickEvent event) {
        if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;
        if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Mechanics.getInstance(), NAMESPACED_CONTENT_KEY), PersistentDataType.STRING)){
            event.setCancelled(true);
            String data = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Mechanics.getInstance(), NAMESPACED_CONTENT_KEY), PersistentDataType.STRING);
            String[] split = data.split(":");
            int index = Integer.parseInt(split[0]);
            int slot = Integer.parseInt(split[1]);
            int page = Integer.parseInt(split[2]);
            T content = searchList.get(index);
            ContentClickAnvilEvent<T> contentClickAnvilEvent = new ContentClickAnvilEvent<T>(event, content, slot, page);
            onContentClick(contentClickAnvilEvent);
            contentClickListeners.forEach(contentClickAnvilEventConsumer -> contentClickAnvilEventConsumer.accept(contentClickAnvilEvent));

        }
    }

    //********** CONTENT **********

    public List<T> getContentList() {
        return contentList;
    }

    public void setContentList(List<T> contentList) {
        this.contentList = contentList;
    }

    public void addContent(T content){
        contentList.add(content);
    }

    public void removeContent(T content){
        contentList.remove(content);
    }

    public void clearContent(int page){
        for(Map.Entry<Integer, T> entry : getContentPage(page).entrySet()){
            player.getInventory().setItem(entry.getKey(), null);
        }
    }

    //********** PAGE **********
    public int getActualPage() {
        return page;
    }

    public void setActualPage(int page) {
        if(page < 0 || page > getMaxPage()) return;
        this.page = page;
    }

    public int nextPage(int page) {
        int a = page * dataSlots.size();
        if(a < searchList.size()){
            return page + 1;
        }
        return -1;
    }
    public int previousPage(int page) {
        if(page > 0){
            return page - 1;
        }
        return -1;
    }
    public int getMaxPage() {
        int a = searchList.size() / dataSlots.size();
        if(searchList.size() % dataSlots.size() != 0){
            a++;
        }
        return a - 1 == -1 ? 0 : a - 1;
    }
    public int getMinPage() {
        return 0;
    }
    public boolean isPossibleNextPage(int page){
        int a = (page * dataSlots.size()) + dataSlots.size();
        return a < searchList.size();
    }
    public boolean isPossiblePreviousPage(int page){
        return page > 0;
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

    public void clearDataSlots(){
        for(int slot : dataSlots){
            player.getInventory().setItem(slot, null);
        }
    }

    //************** CONTENT PAGE **************

    public HashMap<Integer, T> getContentPage(int page){
        HashMap<Integer, T> contentPage = new HashMap<>();
        int a = page * dataSlots.size();
        for (int i : dataSlots) {
            if(a >= searchList.size()){
                break;
            }
            contentPage.put(i, searchList.get(a));
            a++;
        }
        return contentPage;
    }

    public ItemStack getItemStackRename() {
        return itemStackRename;
    }

    public void setContent(int page){
        clearDataSlots();
        for(Map.Entry<Integer, T> entry : getContentPage(page).entrySet()){
            ItemStack itemStack = onContentPage(page, entry.getKey(), entry.getValue());
            if(itemStack == null || itemStack.getItemMeta() == null) itemStack = new ItemBuilderMechanic(Material.BOOK).setName(entry.getValue().toString()).build();
            ItemMeta itemMeta = itemStack.getItemMeta();
            String data = searchList.indexOf(entry.getValue()) + ":" + entry.getKey() + ":" + page;
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(Mechanics.getInstance(), NAMESPACED_CONTENT_KEY), PersistentDataType.STRING, data);
            itemStack.setItemMeta(itemMeta);
            player.getInventory().setItem(entry.getKey(), itemStack);
        }
    }
    //************** ITEM INTERFACE **************
    //******* Set & Remove ********

    public void setItemInterfaceInvPlayer(ItemInterface itemInterface) {
        player.getInventory().setItem(itemInterface.getSlot(), itemInterface.getItemModified());
    }

    public void setItemInterfaceInvPlayer(ItemInterface[] itemInterfaces){
        for(ItemInterface itemInterface : itemInterfaces){
            player.getInventory().setItem(itemInterface.getSlot(), itemInterface.getItemModified());
        }
    }

    public void setItemInterfaceInvPlayer(ItemInterface item, int slot){
        player.getInventory().setItem(slot, item.getItemModified());
    }
    //ARRAY only slots
    public void setItemInterfaceInvPlayer(ItemInterface item, int[] slots){
        for(int slot : slots){
            player.getInventory().setItem(slot, item.getItemModified());
        }
    }

    public void setItemInterfaceInvPlayer(String id){
        if(!existItemInterface(id)) return;
        player.getInventory().setItem(getItemInterfaces().get(id).getSlot(), getItemInterfaces().get(id).getItemModified());
    }

    public void removeItemInterfaceInvPlayer(String id){
        player.getInventory().setItem(getItemInterfaces().get(id).getSlot(), null);
    }
    public void removeItemInterfaceInvPlayer(ItemInterface itemInterface){
        player.getInventory().setItem(itemInterface.getSlot(), null);
    }

    public void removeItemInterfaceInvPlayer(ItemInterface[] itemInterfaces){
        for(ItemInterface itemInterface : itemInterfaces){
            player.getInventory().setItem(itemInterface.getSlot(), null);
        }
    }

    //******************** BUTTONS ********************

    public void setButtonsPage(int page) {
        if(getItemBack() == null || getItemNext() == null) return;
        if(page == getMinPage() ){
            player.getInventory().setItem(getItemBack().getSlot(), null);
        }
        if( page == getMaxPage() ){
            player.getInventory().setItem(getItemNext().getSlot(), null);
        }
        if(page > getMinPage() ) {
            player.getInventory().setItem(getItemBack().getSlot(), getItemBack().getItemModified());
        }
        if(page < getMaxPage() ) {
            player.getInventory().setItem(getItemNext().getSlot(), getItemNext().getItemModified());
        }
    }
    public void removeButtonsPage() {
        if(getItemBack() == null || getItemNext() == null) return;
        player.getInventory().setItem(getItemBack().getSlot(), null);
        player.getInventory().setItem(getItemNext().getSlot(), null);
    }

    public ItemInterface getItemBack() {
        return itemPreviousPage;
    }

    public ItemInterface getItemNext() {
        return itemNextPage;
    }

    public void setItemBack(ItemInterface itemBack) {
        this.itemPreviousPage = itemBack;
        registerItemInterface(itemBack);
    }

    public void setItemNext(ItemInterface itemNext) {
        this.itemNextPage = itemNext;
        registerItemInterface(itemNext);
    }

    public void setItemStackRename(ItemStack itemStackRename) {
        this.itemStackRename = itemStackRename;
        setItem(0, this.itemStackRename, event -> {
            event.setCancelled(true);
        });
        setItem(1, this.itemStackRename, event -> {
            event.setCancelled(true);
        });
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack[] getPlayerInventory() {
        return playerInventory;
    }
}
