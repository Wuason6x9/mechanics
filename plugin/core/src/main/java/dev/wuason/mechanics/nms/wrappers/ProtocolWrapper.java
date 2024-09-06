package dev.wuason.mechanics.nms.wrappers;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface ProtocolWrapper extends Listener {

    public void injectPlayer(Player player);

    public void uninjectPlayer(Player player);

}
