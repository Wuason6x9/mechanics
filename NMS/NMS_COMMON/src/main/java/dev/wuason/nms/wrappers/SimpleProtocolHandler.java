package dev.wuason.nms.wrappers;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface SimpleProtocolHandler extends Listener {

    public void injectPlayer(Player player);

    public void uninjectPlayer(Player player);

}
