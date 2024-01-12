package dev.wuason.mechanics.actions.events;

import dev.wuason.mechanics.actions.ActionManager;
import dev.wuason.mechanics.actions.events.defbukkit.OnJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Events implements Listener {

    private final Plugin plugin;
    private final ActionManager actionManager;

    public static final HashMap<String, Class<? extends EventAction>> EVENTS = new HashMap<>();

    public Events(Plugin plugin, ActionManager actionManager) {
        this.plugin = plugin;
        this.actionManager = actionManager;
    }

    static {

        EVENTS.put("onJoin".toUpperCase(), OnJoinEvent.class);

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!actionManager.isListenDefEvents()) return;
        EventAction eventAction = new OnJoinEvent(event);
        actionManager.callEvent(eventAction, event.getPlayer().getPlayer().getUniqueId().toString());
    }

}
