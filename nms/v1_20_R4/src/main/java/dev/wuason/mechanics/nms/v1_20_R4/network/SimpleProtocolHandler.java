package dev.wuason.mechanics.nms.v1_20_R4.network;

import com.google.common.collect.MapMaker;
import dev.wuason.mechanics.nms.others.AnvilInventoryHolder;
import dev.wuason.mechanics.nms.others.Constants;
import dev.wuason.mechanics.nms.wrappers.ProtocolWrapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;

public final class SimpleProtocolHandler implements ProtocolWrapper {

    private final Map<UUID, Channel> playerChannels = new MapMaker().weakValues().makeMap();
    private final Plugin core;
    private boolean closed = false;
    private final WeakHashMap<Player, List<PacketListener>> packetListenerPlayer = new WeakHashMap<>();

    public SimpleProtocolHandler(Plugin core) {
        this.core = core;
        Bukkit.getPluginManager().registerEvents(this, core);
        Bukkit.getScheduler().runTask(core, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                injectPlayer(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        injectPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(core)) {
            close();
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        unRegisterListeners(event.getPlayer());
        playerChannels.remove(event.getPlayer().getUniqueId());
    }

    @Override
    public void injectPlayer(Player player) {
        if (closed) return;
        if (getChannel(player) == null) {
            Connection connection = getConnection(player);
            Channel ch = connection.channel;
            if (ch.pipeline().get(Constants.CHANNEL_NAME) == null) ch.pipeline().addBefore(Constants.PACKET_HANDLER_NAME, Constants.CHANNEL_NAME, new PacketHandler(player));
        }
    }

    public void unRegisterListeners(Player player) {
        if (packetListenerPlayer.containsKey(player)) {
            packetListenerPlayer.remove(player);
        }
    }

    @Override
    public void uninjectPlayer(Player player) {
        if (closed) return;
        unRegisterListeners(player);
        Channel channel = playerChannels.remove(player.getUniqueId());
        if (channel != null && channel.pipeline().get(Constants.CHANNEL_NAME) != null) {
            channel.eventLoop().execute(() -> channel.pipeline().remove(Constants.CHANNEL_NAME));
        }
    }


    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        for (Player player : Bukkit.getOnlinePlayers()) {
            uninjectPlayer(player);
        }
    }


    public Object onAsyncPacketReceive(Player player, Object packet) {
        //ANVIL GUI
        if (packet instanceof ServerboundRenameItemPacket renameItemPacket) {
            Inventory inventory = player.getOpenInventory().getTopInventory();
            if (inventory instanceof AnvilInventory anvilInventory && inventory.getHolder() != null && inventory.getHolder() instanceof AnvilInventoryHolder anvilInventoryHolder) {
                anvilInventoryHolder.handleRenameTextAsync(anvilInventory.getRenameText(), renameItemPacket.getName());
            }
        }
        return packet;
    }

    public Object onAsyncPacketSend(Player player, Object packet) {
        return packet;
    }

    public PacketListener addPacketListener(Player player, Class<?> packetClass, boolean isIncoming, boolean delete, Function<Object, Object> handler) {
        if (!packetListenerPlayer.containsKey(player)) {
            packetListenerPlayer.put(player, new ArrayList<>());
        }
        PacketListener listener = new PacketListener(handler, packetClass, isIncoming, player, delete);
        packetListenerPlayer.get(player).add(listener);
        return listener;
    }

    public PacketListener addPacketListener(Player player, Class<?> packetClass, boolean isIncoming, Function<Object, Object> handler) {
        return addPacketListener(player, packetClass, isIncoming, false, handler);
    }

    public void removePacketListener(Player player, PacketListener listener) {
        if (packetListenerPlayer.containsKey(player)) {
            packetListenerPlayer.get(player).remove(listener);
        }
    }


    public Channel getChannel(Player player) {
        return playerChannels.get(player.getUniqueId());
    }

    private ServerPlayer getServerPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    private Connection getConnection(Player player) {
        return getServerPlayer(player).connection.connection;
    }


    public final class PacketHandler extends ChannelDuplexHandler {

        protected volatile Player player;

        public PacketHandler(Player player) {
            this.player = player;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            try {
                List<PacketListener> packetListeners = packetListenerPlayer.get(player);
                if (packetListeners != null) {
                    List<PacketListener> toRemove = new ArrayList<>();
                    for (PacketListener listener : packetListeners) {
                        if (listener.getPacketClass().isInstance(msg) && listener.isIncoming()) {
                            msg = listener.getFunction().apply(msg);
                            if (listener.isDelete()) {
                                toRemove.add(listener);
                            }
                        }
                    }
                    packetListeners.removeAll(toRemove);
                }
                msg = onAsyncPacketReceive(player, msg);
            } catch (Exception e) {
                core.getLogger().log(Level.SEVERE, "Error while handling packet receive", e);
            }
            if (msg != null) super.channelRead(ctx, msg);
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            try {
                List<PacketListener> packetListeners = packetListenerPlayer.get(player);
                if (packetListeners != null) {
                    List<PacketListener> toRemove = new ArrayList<>();
                    for (PacketListener listener : packetListeners) {
                        if (listener.getPacketClass().isInstance(msg) && !listener.isIncoming()) {
                            msg = listener.getFunction().apply(msg);
                            if (listener.isDelete()) {
                                toRemove.add(listener);
                            }
                        }
                    }
                    packetListeners.removeAll(toRemove);
                }
                msg = onAsyncPacketSend(player, msg);
            } catch (Exception e) {
                core.getLogger().log(Level.SEVERE, "Error while handling packet send", e);
            }

            if (msg != null) super.write(ctx, msg, promise);
        }

    }
}
