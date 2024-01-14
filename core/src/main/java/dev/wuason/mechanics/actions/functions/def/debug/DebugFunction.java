package dev.wuason.mechanics.actions.functions.def.debug;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.functions.Function;
import dev.wuason.mechanics.actions.functions.FunctionArgument;
import dev.wuason.mechanics.actions.functions.FunctionArgumentProperties;
import dev.wuason.mechanics.actions.functions.FunctionProperties;
import dev.wuason.mechanics.utils.AdventureUtils;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class DebugFunction extends Function {

    public static final Map<String, FunctionArgument> args = Map.of(
            "MESSAGE", new FunctionArgument("MESSAGE", 0, new FunctionArgumentProperties.Builder().build()) {
                @Override
                public Object computeArg(String line, Action action, Object... args) {
                    if(line == null) return "NULL";
                    return line;
                }
            }
    );

    public DebugFunction() {
        super("debug", args, new FunctionProperties.Builder().build());
    }

    @Override
    public boolean execute(Action action, Object... Args) {
        AdventureUtils.sendMessagePluginConsole((Plugin) action.getCore(),  (Args[0]).toString());
        return false;
    }
}
