package dev.wuason.mechanics.actions.events.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.events.EventAction;

import java.util.Locale;

public class Generic implements EventAction {
    @Override
    public void registerPlaceholders(Action action) {
    }

    @Override
    public String getId() {
        return "default".toUpperCase(Locale.ENGLISH);
    }
}
