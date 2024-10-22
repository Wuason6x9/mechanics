package dev.wuason.mechanics.nms.wrappers;


import dev.wuason.mechanics.nms.others.AnvilInventoryHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.function.Consumer;

public interface VersionWrapper {
    /**
     * Retrieves the current version.
     *
     * @return the version as a String.
     */
    public String getVersion();

    /**
     * Creates a custom anvil inventory for a specific player with a given title and holder.
     *
     * @param player The player for whom the inventory is being created.
     * @param title The title of the anvil inventory.
     * @param holder The holder that manages the inventory's actions.
     * @return A custom anvil inventory associated with the player and holder.
     */
    public AnvilInventoryCustom createAnvilInventory(Player player, String title, AnvilInventoryHolder holder);

    public interface AnvilInventoryCustom {
        /**
         * Opens the anvil inventory for interaction.
         *
         * This method sends a packet to the player's client to display the anvil
         * inventory interface. It initializes the player's menu container with the anvil
         * menu and triggers the InventoryOpenEvent.
         */
        public void open();

        /**
         * Opens the anvil inventory for interaction with a custom title.
         *
         * @param title the custom title to display on the anvil inventory
         */
        public void open(String title);

        /**
         * Opens the custom anvil inventory for the specified player.
         *
         * @param player the player for whom the anvil inventory will be opened
         */
        public void open(Player player);

        /**
         * Retrieves the underlying NMS (Net Minecraft Server) representation of the anvil menu.
         *
         * @return an object representing the NMS anvil menu.
         */
        public Object getAnvilMenuNMS();

        /**
         * Retrieves the holder of the anvil inventory.
         *
         * @return an instance of AnvilInventoryHolder associated with the anvil inventory.
         */
        public AnvilInventoryHolder getHolder();

        /**
         * Retrieves the inventory associated with the anvil.
         *
         * @return the AnvilInventory instance linked to the current anvil.
         */
        public AnvilInventory getInventory();

        /**
         * Retrieves the current view of the inventory for this anvil.
         *
         * @return the InventoryView instance representing the current state of the anvil inventory.
         */
        public InventoryView getInventoryView();

        /**
         * Sets the maximum repair cost for the anvil inventory.
         *
         * @param cost the maximum repair cost to be set
         */
        public void setMaxRepairCost(int cost);

        /**
         * Sets the cost in terms of item count required for a repair operation in the anvil inventory.
         *
         * @param cost The number of items required for the repair.
         */
        public void setRepairItemCountCost(int cost);

        /**
         * Sets the rename text in the custom anvil inventory.
         *
         * @param renameText the text to set for the item being renamed
         */
        public void setRenameText(String renameText);

        /**
         * Sets the title of the Anvil Inventory.
         *
         * @param title the new title to set for the inventory
         */
        public void setTitle(String title);

        /**
         * Sets whether the anvil inventory should check if it is reachable by the player.
         *
         * @param r a boolean indicating whether the reachability check should be enforced
         */
        public void setCheckReachable(boolean r);
    }

    /**
     * Updates the title of the current inventory for the specified player.
     *
     * @param jsonTitle The new title to set, formatted as a JSON string.
     * @param player The player whose inventory title is to be updated.
     */
    public void updateCurrentInventoryTitle(String jsonTitle, Player player);

    /**
     * Sends a packet to the specified player to close their currently open inventory.
     *
     * @param player The player whose inventory will be closed.
     */
    public void sendCloseInventoryPacket(Player player);

    public enum ToastType {
        TASK,
        CHALLENGE,
        GOAL
    }

    /**
     * Sends a toast notification to a player.
     *
     * @param player the player to receive the toast notification
     * @param icon the icon to be displayed on the toast
     * @param titleJson the title of the toast, formatted in JSON
     * @param toastType the type of toast, defined in ToastType (TASK, CHALLENGE, GOAL)
     */
    public void sendToast(Player player, ItemStack icon, String titleJson, ToastType toastType);

    /**
     * Sends a toast notification to a player with the specified icon, title, toast type,
     * namespace, and path. This is commonly used to announce achievements or goals in the game.
     *
     * @param player the player who will receive the toast notification
     * @param icon the icon to be displayed in the toast notification
     * @param titleJson the title text of the toast notification in JSON format
     * @param toastType the type of the toast, which can be TASK, CHALLENGE, or GOAL
     * @param namespace the namespace for the custom toast
     * @param path the path for the custom toast
     */
    public void sendToast(Player player, ItemStack icon, String titleJson, ToastType toastType, String namespace, String path);

    /**
     * Opens a singing interface for the specified player.
     *
     * @param player The player for whom the singing interface is to be opened.
     * @param onSend A callback function to handle the text input from the player.
     */
    public void openSing(Player player, Consumer<String[]> onSend);

    /**
     * Opens a sign editor for the specified player with default lines provided.
     *
     * @param player   The player for whom the sign editor is opened.
     * @param defLines An array of default lines to be displayed on the sign. Maximum length is 4.
     * @param onSend   A Consumer that processes the lines entered by the player when the sign is completed.
     */
    public void openSing(Player player, String[] defLines, Consumer<String[]> onSend);

    /**
     * Converts a Bukkit ItemStack to its corresponding NMS (net.minecraft.server) ItemStack.
     *
     * @param itemStack The Bukkit ItemStack to be converted.
     * @return The corresponding NMS ItemStack object.
     */
    public Object getNMSItemStack(ItemStack itemStack);

    /**
     * Converts an NMS (Net Minecraft Server) ItemStack to a Bukkit ItemStack.
     *
     * @param nmsItemStack The NMS ItemStack object to be converted.
     * @return The equivalent Bukkit ItemStack.
     */
    public ItemStack getBukkitItemStack(Object nmsItemStack);

    /**
     * Converts a JSON string into a text component object.
     *
     * @param json the JSON string representing the text component
     * @return an Object representing the text component
     */
    public Object getTextComponent(String json);

    /**
     * Converts a component object to its JSON representation.
     *
     * @param component the component object to be converted to JSON
     * @return a JSON string representation of the component
     */
    public String getJsonFromComponent(Object component);

    /**
     * Retrieves the server handle object.
     * This method is typically used to obtain the underlying NMS server instance
     * from the Bukkit/Spigot API.
     *
     * @return The server handle object, usually an NMS (net.minecraft.server) server instance.
     */
    public Object getServerHandle();

    /**
     * Retrieves the dedicated server instance.
     *
     * @return the dedicated server instance as an Object.
     */
    public Object getDedicatedServer();

    /**
     * Retrieves a simple protocol handler for managing player injections.
     *
     * @return an instance of ProtocolWrapper which allows for injecting and uninjecting players.
     */
    public ProtocolWrapper getSimpleProtocolHandler();
}
