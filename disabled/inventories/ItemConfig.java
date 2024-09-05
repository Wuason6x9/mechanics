package dev.wuason.mechanics.configuration.inventories;

import java.util.ArrayList;
import java.util.List;

public class ItemConfig {
    private final String id;
    private final String itemId;
    private final int amount;
    private final int[] slots;
    private final String actionId;

    public ItemConfig(String id, String itemId, int amount, List<Integer> slots, String actionId) {
        this.id = id;
        this.itemId = itemId;
        this.amount = amount;
        if(slots == null) slots = new ArrayList<>();
        this.slots = slots.stream().mapToInt(Integer::intValue).toArray();
        this.actionId = actionId.toUpperCase();
    }

    public String getId() {
        return this.id;
    }

    public String getItemId() {
        return this.itemId;
    }

    public int getAmount() {
        return this.amount;
    }

    public int[] getSlots() {
        return this.slots;
    }

    public String getActionId() {
        return this.actionId;
    }
}
