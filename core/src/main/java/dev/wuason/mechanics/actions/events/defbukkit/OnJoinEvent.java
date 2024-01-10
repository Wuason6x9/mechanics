package dev.wuason.mechanics.actions.events.defbukkit;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.events.EventBukkit;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Locale;

public class OnJoinEvent extends EventBukkit {
    public OnJoinEvent(PlayerJoinEvent event) {
        super(event);
    }

    @Override
    public void registerPlaceholders(Action action) {
        super.registerPlaceholders(action);

        action.registerPlaceholder("$player$", ((PlayerJoinEvent) getEvent()).getPlayer());

    }

    @Override
    public String getId() {
        return "onJoin".toUpperCase(Locale.ENGLISH);
    }
}
