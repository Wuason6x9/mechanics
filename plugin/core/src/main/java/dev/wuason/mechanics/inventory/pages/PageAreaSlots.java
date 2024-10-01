package dev.wuason.mechanics.inventory.pages;

import java.util.List;

public interface PageAreaSlots {
    public List<Integer> getAreaSlots();

    public void setAreaSlots(List<Integer> slots);

    public void addAreaSlot(int slot);

    public void removeAreaSlot(int slot);

    public void clearAreaSlots();

    public void addAreaSlots(List<Integer> slots);
}
