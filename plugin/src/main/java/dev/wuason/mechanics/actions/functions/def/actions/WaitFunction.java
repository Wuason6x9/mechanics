package dev.wuason.mechanics.actions.functions.def.actions;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.functions.Function;
import dev.wuason.mechanics.actions.functions.FunctionArgument;
import dev.wuason.mechanics.actions.functions.FunctionArgumentProperties;
import dev.wuason.mechanics.actions.functions.FunctionProperties;
import dev.wuason.mechanics.utils.TimeUnitsUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class WaitFunction extends Function {

    public static final Map<String, FunctionArgument> ARGS = Map.of(
            "TIME", new FunctionArgument("TIME", 0, new FunctionArgumentProperties.Builder().build()) {
                @Override
                public Object computeArg(String line, Action action, Object... args) {
                    return TimeUnitsUtils.parseTime(line);
                }
            }
    );

    public WaitFunction() {
        super("WAIT", ARGS, new FunctionProperties.Builder().build());
    }

    @Override
    public boolean execute(Action action, Object... Args) {

        Bukkit.getScheduler().runTaskLater((Plugin)action.getCore(), () -> {
            action.executeNext();
        }, (Long) Args[0]);

        return true;
    }
}
