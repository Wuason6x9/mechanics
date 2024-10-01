package dev.wuason.mechanics.inventory.pages;

import dev.wuason.mechanics.inventory.InvCustom;
import dev.wuason.mechanics.inventory.items.def.pages.ContentItem;
import dev.wuason.mechanics.inventory.items.def.pages.PageButton;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Page<T> implements Pageable, PageContent<T>, SlotsData, PageAreaSlots {

    private final List<T> content = new ArrayList<>();
    private final List<Integer> slots = new ArrayList<>();
    private final List<Integer> areaSlots = new ArrayList<>();
    private final InvCustom invCustom;

    public Page(InvCustom invCustom) {
        this.invCustom = invCustom;
    }

    private int currentPage = 0;

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public void setContent(List<T> content) {
        this.content.clear();
        this.content.addAll(content);
    }

    @Override
    public void addContent(T content) {
        this.content.add(content);
    }

    @Override
    public void removeContent(T content) {
        this.content.remove(content);
    }

    @Override
    public void clearContent() {
        this.content.clear();
    }

    @Override
    public void addAllContent(List<T> content) {
        this.content.addAll(content);
    }

    @Override
    public void handleClickContent(InventoryClickEvent event, ContentItem<T> contentItem) {

    }

    @Override
    public ItemStack handleContentPage(int slot, int page, T content) {
        return null;
    }

    @Override
    public void handleUpdate() {

    }

    @Override
    public void handlePreviousPage(PageButton pageButton) {

    }

    @Override
    public void handleNextPage(PageButton pageButton) {

    }

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
        return PageUtils.getMaxPage(content.size(), slots.size());
    }

    @Override
    public int getMinPage() {
        return 0;
    }

    @Override
    public void updatePage() {

    }

    @Override
    public boolean nextPage() {
        return false;
    }

    @Override
    public boolean previousPage() {
        return false;
    }

    // SlotsData

    @Override
    public List<Integer> getSlotsData() {
        return slots;
    }

    @Override
    public void setSlotsData(List<Integer> slots) {
        this.slots.clear();
        this.slots.addAll(slots);
    }

    @Override
    public void addSlotData(int slot) {
        this.slots.add(slot);
    }

    @Override
    public void removeSlotData(int slot) {
        this.slots.remove(slot);
    }

    @Override
    public void clearSlotsData() {
        this.slots.clear();
    }

    @Override
    public void addSlotsData(List<Integer> slots) {
        this.slots.addAll(slots);
    }

    @Override
    public List<Integer> getAreaSlots() {
        return this.areaSlots;
    }

    @Override
    public void setAreaSlots(List<Integer> slots) {
        this.areaSlots.clear();
        this.areaSlots.addAll(slots);
    }

    @Override
    public void addAreaSlot(int slot) {
        this.areaSlots.add(slot);
    }

    @Override
    public void removeAreaSlot(int slot) {
        this.areaSlots.remove(slot);
    }

    @Override
    public void clearAreaSlots() {
        this.areaSlots.clear();
    }

    @Override
    public void addAreaSlots(List<Integer> slots) {
        this.areaSlots.addAll(slots);
    }
}
