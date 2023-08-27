package dev.wuason.nms.nms_1_19_R3;

import dev.wuason.nms.wrappers.VersionWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R3.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VersionWrapper_1_19_R3 implements VersionWrapper {
    public String getVersion(){
        CraftServer craftServer = (CraftServer) Bukkit.getServer();
        return craftServer.getServer().getServerVersion();
    }

    @Override
    public VersionWrapper.AnvilGui createAnvilGui(Player player, String title, ItemStack repairItem){
        return new AnvilGui(player, title, repairItem);
    }

    public class AnvilGui implements VersionWrapper.AnvilGui {
        private int invId = 0;
        private Player player;
        private Inventory inventory;
        private ServerPlayer serverPlayer;
        private AnvilMenu anvilMenu;
        private String title;
        private boolean blockClose = false;
        private boolean open = false;
        private ItemStack repairItem = null;

        public AnvilGui(Player player, String title, ItemStack repairItem){
            serverPlayer = ((CraftPlayer)player).getHandle();
            this.player = player;
            this.title = title;
            this.repairItem = repairItem;
        }
        @Override
        public AnvilInventory getAnvilInventory(){
            return (AnvilInventory) inventory;
        }
        @Override
        public Object getAnvilMenuNMS(){
            return anvilMenu;
        }
        @Override
        public void callCloseInventoryEvent(){
            CraftEventFactory.handleInventoryCloseEvent(serverPlayer);
        }
        @Override
        public void setDefMenu(){
            serverPlayer.containerMenu = serverPlayer.inventoryMenu;
        }
        @Override
        public void open(){

            open = true;

            invId = serverPlayer.nextContainerCounter();

            ContainerLevelAccess containerLevelAccess = ContainerLevelAccess.create(serverPlayer.getLevel(),new BlockPos(0,0,0));
            AnvilMenu anvilMenu = new AnvilMenu(invId, serverPlayer.getInventory(), containerLevelAccess);

            this.anvilMenu = anvilMenu;

            anvilMenu.setTitle(Component.literal(title));
            anvilMenu.repairItemCountCost = 0;
            anvilMenu.maximumRepairCost = 0;
            anvilMenu.checkReachable = false;

            inventory = anvilMenu.getBukkitView().getTopInventory();

            if(repairItem != null) inventory.setItem(0, repairItem);
            if(repairItem != null) inventory.setItem(1, repairItem);

            ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(invId, MenuType.ANVIL,Component.literal(title));
            serverPlayer.connection.send(packet);
            serverPlayer.containerMenu = anvilMenu;
            serverPlayer.initMenu(anvilMenu);
        }
        @Override
        public void close(){
            open = false;
            CraftEventFactory.handleInventoryCloseEvent(serverPlayer);
            serverPlayer.containerMenu = serverPlayer.inventoryMenu;
            ClientboundContainerClosePacket packet = new ClientboundContainerClosePacket(invId);
            serverPlayer.connection.send(packet);
        }

        @Override
        public int getInvId() {
            return invId;
        }
        @Override
        public Player getPlayer() {
            return player;
        }
        @Override
        public Inventory getInventory() {
            return inventory;
        }
        @Override
        public String getTitle() {
            return title;
        }
        @Override
        public void setTitle(String title) {
            this.title = title;
        }
        @Override
        public boolean isBlockClose() {
            return blockClose;
        }
        @Override
        public void setBlockClose(boolean blockClose) {
            this.blockClose = blockClose;
        }
        @Override
        public boolean isOpen() {
            return open;
        }
        @Override
        public void setOpen(boolean open) {
            this.open = open;
        }
        @Override
        public ItemStack getRepairItem() {
            return repairItem;
        }
        @Override
        public void setRepairItem(ItemStack repairItem) {
            this.repairItem = repairItem;
        }
    }
}
