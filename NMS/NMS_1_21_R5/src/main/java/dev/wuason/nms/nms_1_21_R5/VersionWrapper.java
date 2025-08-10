package dev.wuason.nms.nms_1_21_R5;

import dev.wuason.nms.wrappers.DataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.view.CraftAnvilView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;


public class VersionWrapper implements dev.wuason.nms.wrappers.VersionWrapper {
    public String getVersion() {
        CraftServer craftServer = (CraftServer) Bukkit.getServer();
        return craftServer.getServer().getServerVersion();
    }

    @Override
    public dev.wuason.nms.wrappers.VersionWrapper.AnvilInventoryCustom createAnvilInventory(Player player, String title, InventoryHolder holder) {
        return new AnvilInventoryCustom(player, title, holder);
    }

    @Override
    public Object getNMSItemStack(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    @Override
    public ItemStack getBukkitItemStack(Object nmsItemStack) {
        return CraftItemStack.asBukkitCopy((net.minecraft.world.item.ItemStack) nmsItemStack);
    }

    @Override
    public Object getTextComponent(String json) {
        CraftServer craftServer = (CraftServer) Bukkit.getServer();
        //return Component.Serializer.fromJson(json, craftServer.getHandle().getServer().registryAccess());
        net.kyori.adventure.text.Component adventureComponent = GsonComponentSerializer.gson().deserialize(json);
        return io.papermc.paper.adventure.PaperAdventure.asVanilla(adventureComponent);
    }

    @Override
    public String getJsonFromComponent(Object component) {
        CraftServer craftServer = (CraftServer) Bukkit.getServer();
        //return Component.Serializer.toJson((Component) component, craftServer.getHandle().getServer().registryAccess());
        net.kyori.adventure.text.Component adventureComponent = io.papermc.paper.adventure.PaperAdventure.asAdventure((net.minecraft.network.chat.Component) component);
        return GsonComponentSerializer.gson().serialize(adventureComponent);
    }

    public class AnvilInventoryCustom implements dev.wuason.nms.wrappers.VersionWrapper.AnvilInventoryCustom {
        private final AnvilInventory inventory;
        private final AnvilMenu anvilMenu;
        private final ServerPlayer serverPlayer;
        private final InventoryHolder holder;
        private final InventoryView inventoryView;

        public AnvilInventoryCustom(Player player, String title, InventoryHolder holder) {
            //DEF VARS
            serverPlayer = ((CraftPlayer) player).getHandle();
            this.holder = holder;

            //CREATE INVENTORY
            int invId = serverPlayer.nextContainerCounter();
            AnvilMenu anvilMenu = new AnvilMenu(invId, serverPlayer.getInventory()) {

                private CraftAnvilView bukkitEntity;

                @Override
                public @NotNull CraftAnvilView getBukkitView() {
                    if (this.bukkitEntity != null) {
                        return this.bukkitEntity;
                    }

                    org.bukkit.craftbukkit.inventory.CraftInventoryAnvil inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryAnvil(
                            this.access.getLocation(), this.inputSlots, this.resultSlots) {
                        @Override
                        public InventoryHolder getHolder() {
                            return holder;
                        }

                    };
                    this.bukkitEntity = new CraftAnvilView(this.player.getBukkitEntity(), inventory, this);
                    this.bukkitEntity.updateFromLegacy(inventory);
                    return this.bukkitEntity;
                }

            };

            anvilMenu.setTitle(Component.literal(title));

            this.anvilMenu = anvilMenu;
            this.inventory = (AnvilInventory) anvilMenu.getBukkitView().getTopInventory();
            this.inventoryView = anvilMenu.getBukkitView();


        }

        public void open(String title) {
            CraftEventFactory.callInventoryOpenEvent(serverPlayer, anvilMenu);
            ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(anvilMenu.containerId, MenuType.ANVIL, Component.literal(title));
            serverPlayer.connection.send(packet);
            serverPlayer.containerMenu = anvilMenu;
            serverPlayer.initMenu(anvilMenu);
        }

        @Override
        public void open() {
            CraftEventFactory.callInventoryOpenEvent(serverPlayer, anvilMenu);
            ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(anvilMenu.containerId, MenuType.ANVIL, anvilMenu.getTitle());
            serverPlayer.connection.send(packet);
            serverPlayer.containerMenu = anvilMenu;
            serverPlayer.initMenu(anvilMenu);
        }

        @Override
        public void open(Player player) {
            ServerPlayer srvPlayer = ((CraftPlayer) player).getHandle();
            if (player.equals(anvilMenu.getBukkitView().getPlayer())) open();
            int invId = srvPlayer.nextContainerCounter();
            AnvilMenu anvilMenu = new AnvilMenu(invId, serverPlayer.getInventory()) {
                private CraftAnvilView bukkitEntity;

                @Override
                public CraftAnvilView getBukkitView() {
                    if (this.bukkitEntity != null) {
                        return this.bukkitEntity;
                    }
                    this.bukkitEntity = new CraftAnvilView(this.player.getBukkitEntity(), inventory, this);
                    return this.bukkitEntity;
                }
            };
            anvilMenu.setTitle(this.anvilMenu.getTitle());
            CraftEventFactory.callInventoryOpenEvent(serverPlayer, anvilMenu);
            ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(invId, MenuType.ANVIL, anvilMenu.getTitle());
            srvPlayer.connection.send(packet);
            srvPlayer.containerMenu = anvilMenu;
            srvPlayer.initMenu(anvilMenu);
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
        public InventoryHolder getHolder() {
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

        net.kyori.adventure.text.Component adv = GsonComponentSerializer.gson().deserialize(titleJson);
        Component nmsComponent = PaperAdventure.asVanilla(adv);

        Optional<DisplayInfo> displayInfo = Optional.of(new DisplayInfo(
                iconNMS,
                nmsComponent,
                Component.literal("."),
                Optional.empty(),
                AdvancementType.valueOf(toastType.toString()),
                true, false, true
        ));

        AdvancementRewards advancementRewards = AdvancementRewards.EMPTY;
        Optional<ResourceLocation> id = Optional.of(ResourceLocation.fromNamespaceAndPath(namespace, path));
        Criterion<ImpossibleTrigger.TriggerInstance> impossibleTrigger = new Criterion<>(new ImpossibleTrigger(), new ImpossibleTrigger.TriggerInstance());
        HashMap<String, Criterion<?>> criteria = new HashMap<>() {{
            put("impossible", impossibleTrigger);
        }};
        List<List<String>> requirements = new ArrayList<>() {{
            add(new ArrayList<>() {{
                add("impossible");
            }});
        }};
        AdvancementRequirements advancementRequirements = new AdvancementRequirements(requirements);
        Advancement advancement = new Advancement(Optional.empty(), displayInfo, advancementRewards, criteria, advancementRequirements, false);
        Map<ResourceLocation, AdvancementProgress> advancementsToGrant = new HashMap<>();
        AdvancementProgress advancementProgress = new AdvancementProgress();
        advancementProgress.update(advancementRequirements);
        advancementProgress.getCriterion("impossible").grant();
        advancementsToGrant.put(id.get(), advancementProgress);
        ClientboundUpdateAdvancementsPacket packet = new ClientboundUpdateAdvancementsPacket(false, new ArrayList<>() {{
            add(new AdvancementHolder(id.get(), advancement));
        }}, new HashSet<>(), advancementsToGrant, true);
        serverPlayer.connection.send(packet);
        ClientboundUpdateAdvancementsPacket packet2 = new ClientboundUpdateAdvancementsPacket(false, new ArrayList<>(), new HashSet<>() {{
            add(id.get());
        }}, new HashMap<>(), true);
        serverPlayer.connection.send(packet2);
    }

    @Override
    public void updateCurrentInventoryTitle(String jsonTitle, Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        MenuType<?> menuType = serverPlayer.containerMenu.getType();
        int invId = serverPlayer.containerMenu.containerId;
        //CraftServer craftServer = (CraftServer) Bukkit.getServer();

        net.kyori.adventure.text.Component adv = GsonComponentSerializer.gson().deserialize(jsonTitle);
        net.minecraft.network.chat.Component title = io.papermc.paper.adventure.PaperAdventure.asVanilla(adv);

        ClientboundOpenScreenPacket packetOpen = new ClientboundOpenScreenPacket(invId, menuType, title);

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
        Location loc = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
        BlockPos blockPos = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        SignBlockEntity signBlock = new SignBlockEntity(blockPos, Blocks.OAK_SIGN.defaultBlockState());

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
        signBlock.setLevel(serverPlayer.level());
        serverPlayer.connection.send(signBlock.getUpdatePacket());
        signBlock.setLevel(null);
        serverPlayer.connection.send(new ClientboundOpenSignEditorPacket(blockPos, true));
        ChannelPipeline pipeline = serverPlayer.connection.connection.channel.pipeline();
        pipeline.addBefore("packet_handler", DataInfo.NAMESPACE_SIGN, new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
                        if (msg instanceof ServerboundSignUpdatePacket packet) {
                            onSend.accept(packet.getLines());
                            player.sendBlockChange(loc, loc.getBlock().getBlockData());
                            pipeline.remove(DataInfo.NAMESPACE_SIGN);
                        }
                        super.channelRead(ctx, msg);
                    }
                }
        );
    }

    @Override
    public void openSing(Player player, Consumer<String[]> onSend) {
        openSing(player, new String[4], onSend);
    }


    public Object getServerHandle() {
        return (((CraftServer) Bukkit.getServer())).getHandle();
    }

    public Object getDedicatedServer() {
        DedicatedServer dedicatedServer = ((DedicatedPlayerList) getServerHandle()).getServer();
        return dedicatedServer;
    }
}
