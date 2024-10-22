package dev.wuason.mechanics.nms.wrappers;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface ProtocolWrapper extends Listener {

    /**
     * Injects a player into the protocol handling system.
     * This method modifies the player's channel pipeline to include a custom packet handler,
     * allowing the application to intercept and process packets for the specified player.
     *
     * @param player the player to inject into the protocol handling system
     */
    public void injectPlayer(Player player);

    /**
     * Uninjects a player from the protocol wrapper, removing any associated listeners and pipeline handlers.
     *
     * @param player the player to be un-injected
     */
    public void uninjectPlayer(Player player);

}
