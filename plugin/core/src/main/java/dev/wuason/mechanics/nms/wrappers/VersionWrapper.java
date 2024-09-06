package dev.wuason.mechanics.nms.wrappers;


import dev.wuason.mechanics.nms.others.AnvilInventoryHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.function.Consumer;

public interface VersionWrapper {
    public String getVersion();

    public AnvilInventoryCustom createAnvilInventory(Player player, String title, AnvilInventoryHolder holder);

    public interface AnvilInventoryCustom {
        public void open();

        public void open(String title);

        public void open(Player player);

        public Object getAnvilMenuNMS();

        public AnvilInventoryHolder getHolder();

        public AnvilInventory getInventory();

        public InventoryView getInventoryView();

        public void setMaxRepairCost(int cost);

        public void setRepairItemCountCost(int cost);

        public void setRenameText(String renameText);

        public void setTitle(String title);

        public void setCheckReachable(boolean r);
    }

    public void updateCurrentInventoryTitle(String jsonTitle, Player player);

    public void sendCloseInventoryPacket(Player player);

    public enum ToastType {
        TASK,
        CHALLENGE,
        GOAL
    }

    public void sendToast(Player player, ItemStack icon, String titleJson, ToastType toastType);

    public void sendToast(Player player, ItemStack icon, String titleJson, ToastType toastType, String namespace, String path);

    public void openSing(Player player, Consumer<String[]> onSend);

    public void openSing(Player player, String[] defLines, Consumer<String[]> onSend);

    public Object getNMSItemStack(ItemStack itemStack);

    public ItemStack getBukkitItemStack(Object nmsItemStack);

    public Object getTextComponent(String json);

    public String getJsonFromComponent(Object component);

    public Object getServerHandle();

    public Object getDedicatedServer();

    public ProtocolWrapper getSimpleProtocolHandler();
}
