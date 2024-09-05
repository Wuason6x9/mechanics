package dev.wuason.old.types.pages.content.multiple;

import dev.wuason.mechanics.inventory.items.ItemInterface;
import dev.wuason.old.types.pages.content.multiple.items.NextPageItem;
import dev.wuason.old.types.pages.content.multiple.items.PreviousPageItem;
import dev.wuason.mechanics.inventory.InvCustom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PageCustomInfo<T> {
    private int page = 0;
    private UUID id;
    private List<Integer> dataSlots = new ArrayList<>();
    private List<T> contentList = new ArrayList<>();
    private ItemInterface itemBack;
    private ItemInterface itemNext;

    public PageCustomInfo(List<Integer> dataSlots, UUID id, PreviousPageItem itemBack, NextPageItem itemNext) {
        this.dataSlots = dataSlots;
        this.id = id;
        setItemBack(itemBack);
        setItemNext(itemNext);
    }
    public PageCustomInfo(List<Integer> dataSlots, UUID id, PreviousPageItem itemBack, NextPageItem itemNext, List<T> contentList) {
        this.dataSlots = dataSlots;
        this.id = id;
        setContentList(contentList);
        setItemBack(itemBack);
        setItemNext(itemNext);
    }
    public PageCustomInfo(List<Integer> dataSlots, PreviousPageItem itemBack, NextPageItem itemNext) {
        this.dataSlots = dataSlots;
        this.id = UUID.randomUUID();
        setItemBack(itemBack);
        setItemNext(itemNext);
    }
    public PageCustomInfo(List<Integer> dataSlots, PreviousPageItem itemBack, NextPageItem itemNext, List<T> contentList) {
        this.dataSlots = dataSlots;
        this.id = UUID.randomUUID();
        setContentList(contentList);
        setItemBack(itemBack);
        setItemNext(itemNext);
    }

    public void registerItems(InvCustomPagesContentMultiple inventoryCustomPages){
        if(getItemBack() != null) inventoryCustomPages.registerItemInterface(getItemBack());
        if(getItemNext() != null) inventoryCustomPages.registerItemInterface(getItemNext());
    }

    public int getActualPage() {
        return page;
    }
    public List<Integer> getDataSlots() {
        return dataSlots;
    }
    public void setDataSlots(List<Integer> dataSlots) {
        this.dataSlots = dataSlots;
    }
    public void addDataSlot(int dataSlot) {
        this.dataSlots.add(dataSlot);
    }
    public void removeDataSlot(int dataSlot) {
        this.dataSlots.remove(dataSlot);
    }

    public void nextPage() {
        if(isPossibleNextPage()){
            this.page++;
        }
    }
    public void previousPage() {
        if(page > 0){
            this.page--;
        }
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
    public void firstPage() {
        this.page = 0;
    }
    public void lastPage() {
        this.page = Integer.MAX_VALUE;
    }
    public void resetPage() {
        this.page = 0;
    }
    public List<T> getContentList() {
        return contentList;
    }
    public void setContentList(List<T> contentList) {
        this.contentList = new ArrayList<T>(contentList);
    }
    public void addContent(T content) {
        this.contentList.add(content);
    }
    public void removeContent(T content) {
        this.contentList.remove(content);
    }
    public void clearContent() {
        this.contentList.clear();
    }
    public void resetContent() {
        this.contentList.clear();
    }
    public boolean isPossibleNextPage(){
        int a = (page * dataSlots.size()) + dataSlots.size();
        return a < contentList.size();
    }
    public boolean isPossiblePreviousPage(){
        return page > 0;
    }
    public HashMap<Integer, T> getActualPageContent(){
        HashMap<Integer, T> actualPageContent = new HashMap<>();
        int a = page * dataSlots.size();
        for(int i : dataSlots ){
            if(a >= contentList.size()){
                break;
            }
            actualPageContent.put(dataSlots.get(i), contentList.get(a));
            a++;
        }
        return actualPageContent;
    }

    public HashMap<Integer, T> getPageContent(int page){
        if(page >= getMaxPage() || page < 0) return null;
        HashMap<Integer, T> actualPageContent = new HashMap<>();
        int a = page * dataSlots.size();
        for(int i = 0; i < dataSlots.size(); i++){
            if(a >= contentList.size()){
                break;
            }
            actualPageContent.put(dataSlots.get(i), contentList.get(a));
            a++;
        }
        return actualPageContent;
    }

    public void setButtonsPage(InvCustomPagesContentMultiple inventoryCustomPages) {
        if(getItemBack() == null || getItemNext() == null) return;
        if(getActualPage() == getMinPage() ){
            inventoryCustomPages.getInventory().setItem(getItemBack().getSlot(), null);
        }
        if( getActualPage() == getMaxPage() ){
            inventoryCustomPages.getInventory().setItem(getItemNext().getSlot(), null);
        }

        if(getActualPage() > getMinPage() ) {
            inventoryCustomPages.setItemInterfaceInv(getItemBack());
        }
        if(getActualPage() < getMaxPage() ) {
            inventoryCustomPages.setItemInterfaceInv(getItemNext());
        }
    }

    public void removeButtonsPage(InvCustomPagesContentMultiple inventoryCustomPages) {
        if(getItemBack() == null || getItemNext() == null) return;
        inventoryCustomPages.getInventory().setItem(getItemBack().getSlot(), null);
        inventoryCustomPages.getInventory().setItem(getItemNext().getSlot(), null);
    }

    public void clearDataSlots(InvCustom inventoryCustom) {
        for(int i : getDataSlots()){
            inventoryCustom.getInventory().setItem(i, null);
        }
    }

    public void setActualPage(int i) {
        if(i > getMaxPage() || i < getMinPage()) return;
        this.page = i;
    }


    public UUID getId() {
        return id;
    }

    public PreviousPageItem getItemBack() {
        return (PreviousPageItem) itemBack;
    }

    public NextPageItem getItemNext() {
        return (NextPageItem) itemNext;
    }

    public void setItemBack(PreviousPageItem itemBack) {
        if(itemBack == null) return;
        itemBack.getData().add(getId().toString());
        this.itemBack = itemBack;
    }

    public void setItemNext(NextPageItem itemNext) {
        if(itemNext == null) return;
        itemNext.getData().add(getId().toString());
        this.itemNext = itemNext;
    }
}
