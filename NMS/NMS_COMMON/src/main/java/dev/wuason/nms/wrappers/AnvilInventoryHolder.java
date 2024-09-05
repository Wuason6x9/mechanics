package dev.wuason.nms.wrappers;

import org.bukkit.inventory.InventoryHolder;

public interface AnvilInventoryHolder extends InventoryHolder {

    void handleRenameTextAsync(String before, String now);
}
