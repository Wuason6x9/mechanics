package dev.wuason.mechanics.invmechanic.events;

import org.bukkit.event.Cancellable;

public class CustomEvent implements Cancellable {

    private boolean cancelled = false;
    private final String name;

    public CustomEvent(String name) {
        this.name = name;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
    public String getName() {
        return name;
    }

}
