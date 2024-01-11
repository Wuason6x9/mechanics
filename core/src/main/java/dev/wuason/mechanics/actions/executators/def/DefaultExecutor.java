package dev.wuason.mechanics.actions.executators.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.executators.Executor;

public class DefaultExecutor extends Executor {
    public DefaultExecutor(String id) {
        super(id, null);
    }

    @Override
    public void registerPlaceholders(Action action) {
    }
}
