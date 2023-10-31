package dev.wuason.nms.wrappers;


import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface VersionWrapper {
    public String getVersion();
    public AnvilGui createAnvilGui(Player player, String title, ItemStack repairItem);
    public interface AnvilGui {
        public AnvilInventory getAnvilInventory();
        public Object getAnvilMenuNMS();
        public void setDefMenu();
        public void callCloseInventoryEvent();
        public void open();
        public void close();
        public int getInvId();
        public Player getPlayer();
        public Inventory getInventory();
        public String getTitle();
        public void setTitle(String title);
        public boolean isBlockClose();
        public void setBlockClose(boolean blockClose);
        public boolean isOpen();
        public void setOpen(boolean open);
        public ItemStack getRepairItem();
        public void setRepairItem(ItemStack repairItem);
    }
    public void updateCurrentInventoryTitle(String jsonTitle, Player player);
    public enum ToastType{
        TASK,
        CHALLENGE,
        GOAL

    }

    public void sendToast(Player player, ItemStack icon, String titleJson, ToastType toastType);
}
