package dev.wuason.mechanics.actions.events.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.events.EventAction;

import java.util.Locale;

public class ActionCreateEvent implements EventAction {
    private final Action actionExecutor;

    public ActionCreateEvent(Action actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @Override
    public void registerPlaceholders(Action action) {
        action.registerPlaceholder("$actionExecutor$", actionExecutor);
    }

    @Override
    public String getId() {
        return "createAction".toUpperCase(Locale.ENGLISH);
    }
}
