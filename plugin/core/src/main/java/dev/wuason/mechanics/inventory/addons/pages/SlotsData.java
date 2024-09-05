package dev.wuason.mechanics.inventory.addons.pages;

import java.util.List;

public interface SlotsData {
    public List<Integer> getSlots();

    public void setSlots(List<Integer> slots);

    public void addSlot(int slot);

    public void removeSlot(int slot);

    public void clearSlots();

    public void addSlots(List<Integer> slots);
}
