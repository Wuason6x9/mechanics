package dev.wuason.mechanics.nms.v1_18_R2.network;

import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Function;

public class PacketListener {
    private final UUID uuid = UUID.randomUUID();
    private final Function<Object, Object> function;
    private final Class<?> packetClass;
    private final boolean isIncoming;
    private final Player player;
    private final boolean delete;

    public PacketListener(Function<Object, Object> function, Class<?> packetClass, boolean isIncoming, Player player, boolean delete) {
        this.function = function;
        this.packetClass = packetClass;
        this.isIncoming = isIncoming;
        this.player = player;
        this.delete = delete;
    }


    public UUID getUuid() {
        return uuid;
    }

    public Function<Object, Object> getFunction() {
        return function;
    }

    public Class<?> getPacketClass() {
        return packetClass;
    }

    public boolean isIncoming() {
        return isIncoming;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isDelete() {
        return delete;
    }
}
