package dev.wuason.nms.nms_1_20_R1;

import dev.wuason.nms.nms_1_20_R1.network.SimpleProtocolHandler;
import dev.wuason.nms.wrappers.AnvilInventoryHolder;
import dev.wuason.nms.wrappers.Constants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class VersionWrapper implements dev.wuason.nms.wrappers.VersionWrapper {

    public final Plugin plugin;

    public final SimpleProtocolHandler simpleProtocolHandler;

    public VersionWrapper(Plugin plugin) {
        this.plugin = plugin;
        this.simpleProtocolHandler = new SimpleProtocolHandler(plugin);
    }

    public String getVersion() {
        CraftServer craftServer = (CraftServer) Bukkit.getServer();
        return craftServer.getServer().getServerVersion();
    }

    @Override
    public Object getNMSItemStack(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    @Override
    public ItemStack getBukkitItemStack(Object nmsItemStack) {
        return CraftItemStack.asBukkitCopy((net.minecraft.world.item.ItemStack) nmsItemStack);
    }

    public Object getServerHandle() {
        return (((CraftServer) Bukkit.getServer())).getHandle();
    }

    public Object getDedicatedServer() {
        DedicatedServer dedicatedServer = ((DedicatedPlayerList) getServerHandle()).getServer();
        return dedicatedServer;
    }

    @Override
    public dev.wuason.nms.wrappers.SimpleProtocolHandler getSimpleProtocolHandler() {
        return simpleProtocolHandler;
    }

    @Override
    public Object getTextComponent(String json) {
        return Component.Serializer.fromJson(json);
    }

    @Override
    public String getJsonFromComponent(Object component) {
        return Component.Serializer.toJson((Component) component);
    }

    @Override
    public dev.wuason.nms.wrappers.VersionWrapper.AnvilInventoryCustom createAnvilInventory(Player player, String title, AnvilInventoryHolder holder) {
        return new AnvilInventoryCustom(player, title, holder);
    }

    public class AnvilInventoryCustom implements dev.wuason.nms.wrappers.VersionWrapper.AnvilInventoryCustom {
        private final AnvilInventory inventory;
        private final AnvilMenu anvilMenu;
        private final ServerPlayer serverPlayer;
        private final AnvilInventoryHolder holder;
        private final InventoryView inventoryView;

        public AnvilInventoryCustom(Player player, String title, AnvilInventoryHolder holder) {
            //DEF VARS
            serverPlayer = ((CraftPlayer) player).getHandle();
            this.holder = holder;

            //CREATE INVENTORY
            int invId = serverPlayer.nextContainerCounter();
            AnvilMenu anvilMenu = new AnvilMenu(invId, serverPlayer.getInventory()) {

                private CraftInventoryView bukkitEntity;

                @Override
                public CraftInventoryView getBukkitView() {
                    if (this.bukkitEntity != null) {
                        return this.bukkitEntity;
                    }

                    CraftInventory inventory = new CraftInventoryAnvil(access.getLocation(), this.inputSlots, this.resultSlots, this) {
                        @Override
                        public InventoryHolder getHolder() {
                            return holder;
                        }

                    };
                    this.bukkitEntity = new CraftInventoryView(this.player.getBukkitEntity(), inventory, this);
                    return this.bukkitEntity;
                }

            };

            anvilMenu.setTitle(Component.literal(title));

            this.anvilMenu = anvilMenu;
            this.inventory = (AnvilInventory) anvilMenu.getBukkitView().getTopInventory();
            this.inventoryView = anvilMenu.getBukkitView();


        }

        @Override
        public void open(String title) {
            ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(anvilMenu.containerId, MenuType.ANVIL, Component.literal(title));
            serverPlayer.connection.send(packet);
            serverPlayer.containerMenu = anvilMenu;
            serverPlayer.initMenu(anvilMenu);
            Bukkit.getPluginManager().callEvent(new InventoryOpenEvent(anvilMenu.getBukkitView()));
        }

        @Override
        public void open() {
            ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(anvilMenu.containerId, MenuType.ANVIL, anvilMenu.getTitle());
            serverPlayer.connection.send(packet);
            serverPlayer.containerMenu = anvilMenu;
            serverPlayer.initMenu(anvilMenu);
            Bukkit.getPluginManager().callEvent(new InventoryOpenEvent(anvilMenu.getBukkitView()));
        }

        @Override
        public void open(Player player) {
            ServerPlayer srvPlayer = ((CraftPlayer) player).getHandle();
            if (player.equals(anvilMenu.getBukkitView().getPlayer())) open();
            int invId = srvPlayer.nextContainerCounter();
            AnvilMenu anvilMenu = new AnvilMenu(invId, serverPlayer.getInventory()) {
                private CraftInventoryView bukkitEntity;

                @Override
                public CraftInventoryView getBukkitView() {
                    if (this.bukkitEntity != null) {
                        return this.bukkitEntity;
                    }
                    this.bukkitEntity = new CraftInventoryView(this.player.getBukkitEntity(), inventory, this);
                    return this.bukkitEntity;
                }
            };
            anvilMenu.setTitle(this.anvilMenu.getTitle());
            ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(invId, MenuType.ANVIL, anvilMenu.getTitle());
            srvPlayer.connection.send(packet);
            srvPlayer.containerMenu = anvilMenu;
            srvPlayer.initMenu(anvilMenu);
            Bukkit.getPluginManager().callEvent(new InventoryOpenEvent(anvilMenu.getBukkitView()));
        }

        @Override
        public void setCheckReachable(boolean r) {
            anvilMenu.checkReachable = r;
        }

        @Override
        public Object getAnvilMenuNMS() {
            return anvilMenu;
        }

        @Override
        public AnvilInventoryHolder getHolder() {
            return holder;
        }

        @Override
        public @NotNull AnvilInventory getInventory() {
            return inventory;
        }

        @Override
        public InventoryView getInventoryView() {
            return inventoryView;
        }

        @Override
        public void setMaxRepairCost(int cost) {
            anvilMenu.maximumRepairCost = cost;
        }

        @Override
        public void setRepairItemCountCost(int cost) {
            anvilMenu.repairItemCountCost = cost;
        }

        @Override
        public void setRenameText(String renameText) {
            anvilMenu.itemName = renameText;
        }

        @Override
        public void setTitle(String title) {
            anvilMenu.setTitle(Component.nullToEmpty(title));
        }
    }

    @Override
    public void sendToast(Player player, ItemStack icon, String titleJson, ToastType toastType) {
        sendToast(player, icon, titleJson, toastType, "mechanics", "custom_toast");
    }

    @Override
    public void sendToast(Player player, ItemStack icon, String titleJson, ToastType toastType, String namespace, String path) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        net.minecraft.world.item.ItemStack iconNMS = CraftItemStack.asNMSCopy(new ItemStack(Material.AIR));
        if (icon != null) {
            iconNMS = CraftItemStack.asNMSCopy(icon);
        }
        DisplayInfo displayInfo = new DisplayInfo(iconNMS, Component.Serializer.fromJson(titleJson), Component.literal("."), null, FrameType.valueOf(toastType.toString()), true, false, true);
        AdvancementRewards advancementRewards = AdvancementRewards.EMPTY;
        ResourceLocation id = new ResourceLocation(namespace, path);
        Criterion criterion = new Criterion(new ImpossibleTrigger.TriggerInstance());
        HashMap<String, Criterion> criteria = new HashMap<>() {{
            put("impossible", criterion);
        }};
        String[][] requirements = {{"impossible"}};
        Advancement advancement = new Advancement(id, null, displayInfo, advancementRewards, criteria, requirements, false);
        Map<ResourceLocation, AdvancementProgress> advancementsToGrant = new HashMap<>();
        AdvancementProgress advancementProgress = new AdvancementProgress();
        advancementProgress.update(criteria, requirements);
        advancementProgress.getCriterion("impossible").grant();
        advancementsToGrant.put(id, advancementProgress);
        ClientboundUpdateAdvancementsPacket packet = new ClientboundUpdateAdvancementsPacket(false, new ArrayList<>() {{
            add(advancement);
        }}, new HashSet<>(), advancementsToGrant);
        serverPlayer.connection.send(packet);
        ClientboundUpdateAdvancementsPacket packet2 = new ClientboundUpdateAdvancementsPacket(false, new ArrayList<>(), new HashSet<>() {{
            add(id);
        }}, new HashMap<>());
        serverPlayer.connection.send(packet2);
    }

    @Override
    public void updateCurrentInventoryTitle(String jsonTitle, Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        MenuType<?> menuType = serverPlayer.containerMenu.getType();
        int invId = serverPlayer.containerMenu.containerId;
        ClientboundOpenScreenPacket packetOpen = new ClientboundOpenScreenPacket(invId, menuType, Component.Serializer.fromJson(jsonTitle));
        serverPlayer.connection.send(packetOpen);
        serverPlayer.initMenu(serverPlayer.containerMenu);
    }

    @Override
    public void sendCloseInventoryPacket(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(new ClientboundContainerClosePacket(serverPlayer.containerMenu.containerId));
    }

    @Override
    public void openSing(Player player, String[] defLines, Consumer<String[]> onSend) {
        ServerPlayer serverPlayer = (ServerPlayer) ((CraftPlayer) player).getHandle();
        Location loc = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() - 7, player.getLocation().getBlockZ());
        BlockPos blockPos = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        SignBlockEntity signBlock = new SignBlockEntity(blockPos, null);
        Component[] signTexts = new Component[4];
        for (int i = 0; i < 4; i++) {
            if (defLines[i] == null) {
                signTexts[i] = Component.literal("");
                continue;
            }
            signTexts[i] = Component.literal(defLines[i]);
        }
        SignText signText = new SignText(signTexts, signTexts, DyeColor.BLACK, false);
        signBlock.setText(signText, true);
        player.sendBlockChange(loc, Material.OAK_SIGN.createBlockData());
        serverPlayer.connection.send(signBlock.getUpdatePacket());
        serverPlayer.connection.send(new ClientboundOpenSignEditorPacket(blockPos, true));
        simpleProtocolHandler.addPacketListener(player, ServerboundSignUpdatePacket.class, true, true, packet -> {
            onSend.accept(((ServerboundSignUpdatePacket) packet).getLines());
            player.sendBlockChange(loc, loc.getBlock().getBlockData());
            return packet;
        });
    }

    @Override
    public void openSing(Player player, Consumer<String[]> onSend) {
        openSing(player, new String[4], onSend);
    }
}
