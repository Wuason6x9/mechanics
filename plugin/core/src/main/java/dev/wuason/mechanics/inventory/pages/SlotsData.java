package dev.wuason.mechanics.inventory.pages;

import java.util.List;

public interface SlotsData {
    public List<Integer> getSlotsData();

    public void setSlotsData(List<Integer> slots);

    public void addSlotData(int slot);

    public void removeSlotData(int slot);

    public void clearSlotsData();

    public void addSlotsData(List<Integer> slots);
}
