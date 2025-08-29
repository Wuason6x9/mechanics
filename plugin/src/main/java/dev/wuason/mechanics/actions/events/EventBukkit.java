package dev.wuason.mechanics.actions.events;

import dev.wuason.mechanics.actions.Action;
import org.bukkit.event.Event;

public abstract class EventBukkit implements EventAction {
    private final Event event;

    public EventBukkit(Event event) {
        this.event = event;
    }

    @Override
    public void registerPlaceholders(Action action) {
        action.registerPlaceholder("$event$", event);
        registerPlaceholdersBukkit(action);
    }

    public void registerPlaceholdersBukkit(Action action){

    }


    public Event getEvent() {
        return event;
    }
}
