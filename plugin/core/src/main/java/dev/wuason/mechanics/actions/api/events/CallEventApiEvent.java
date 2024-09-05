package dev.wuason.mechanics.actions.api.events;

import dev.wuason.mechanics.actions.events.EventAction;
import org.bukkit.event.Cancellable;

public class CallEventApiEvent implements Cancellable {
    private final EventAction eventAction;
    private final String namespace;
    private final Object[] args;
    private boolean cancelled;

    public CallEventApiEvent(EventAction eventAction, String namespace, Object... args) {
        this.eventAction = eventAction;
        this.namespace = namespace;
        this.args = args;
    }

    public EventAction getEventAction() {
        return eventAction;
    }

    public String getNamespace() {
        return namespace;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
