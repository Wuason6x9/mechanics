package dev.wuason.mechanics.actions.functions.def.vanilla;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.functions.Function;
import dev.wuason.mechanics.actions.functions.FunctionArgument;
import dev.wuason.mechanics.actions.functions.FunctionProperties;
import org.bukkit.event.Cancellable;

import java.util.Map;

public class CancelEventFunction extends Function {
    public CancelEventFunction() {
        super("cancelEvent",
                new FunctionArgument.Builder()
                        .addArgument(0, "cancelled", (s, action, objects) -> {
                            if(s == null) return false;
                            try {
                                return Boolean.parseBoolean(s);
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        }, builder -> builder.setRequired(true).build()).build()
                ,
                new FunctionProperties.Builder().build());
    }

    @Override
    public boolean execute(Action action, Object... Args) {
        Cancellable event = (Cancellable) action.getPlaceholder("$bukkitEvent$");
        event.setCancelled((boolean) Args[0]);
        return false;
    }
}
