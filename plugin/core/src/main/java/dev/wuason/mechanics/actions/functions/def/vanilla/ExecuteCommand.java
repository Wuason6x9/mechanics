package dev.wuason.mechanics.actions.functions.def.vanilla;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.functions.Function;
import dev.wuason.mechanics.actions.functions.FunctionArgument;
import dev.wuason.mechanics.actions.functions.FunctionArgumentProperties;
import dev.wuason.mechanics.actions.functions.FunctionProperties;
import dev.wuason.mechanics.utils.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ExecuteCommand extends Function {

    public static final Map<String, FunctionArgument> ARGS = Collections.unmodifiableMap(new HashMap<>()
    {{
        FunctionArgument asConsole = new FunctionArgument("asConsole", 0, new FunctionArgumentProperties.Builder().build()) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {
                try {
                    return Boolean.parseBoolean(line);
                } catch (Exception e) {
                    return false;
                }
            }

        };

        put(asConsole.getName(), asConsole);

        FunctionArgument player = new FunctionArgument("player", 1, new FunctionArgumentProperties.Builder().build()) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {
                if( (boolean)args[0] ) return null;
                if(line == null) return action.getPlaceholder("$player$");
                if( line.startsWith("$") && line.endsWith("$") && action.getPlaceholder(line) instanceof Player player ) return player;
                UUID uuid = null;
                try {
                    uuid = UUID.fromString(line);
                } catch (Exception e) {}
                if(uuid != null) return Bukkit.getPlayer(uuid);
                Player player = Bukkit.getPlayer(line);
                if(player != null) return player;
                return action.getPlaceholder("$player$");
            }

        };

        put(player.getName(), player);

        FunctionArgument asOp = new FunctionArgument("asOp", 2, new FunctionArgumentProperties.Builder().build()) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {
                if( (boolean)args[0] ) return false;
                if(args[1] == null) return false;
                Player player = (Player)args[1];
                if(player.isOp()) return false;
                try {
                    return Boolean.parseBoolean(line);
                } catch (Exception e) {
                    return false;
                }
            }

        };

        put(asOp.getName(), asOp);

        FunctionArgument command = new FunctionArgument("command", 3, new FunctionArgumentProperties.Builder().build()) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {

                String command = AdventureUtils.deserializeLegacy(line, (Player)args[1]);

                return command;
            }

        };

        put(command.getName(), command);

        FunctionArgument delay = new FunctionArgument("delay", 4, new FunctionArgumentProperties.Builder().build()) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {
                try {
                    return Long.parseLong(line);
                } catch (Exception e) {
                    return 0L;
                }
            }

        };

        put(delay.getName(), delay);

    }}
    );

    public ExecuteCommand() {
        super("executecommand", ARGS, new FunctionProperties.Builder().build());
    }

    @Override
    public boolean execute(Action action, Object... args) {
        boolean asConsole = (boolean) args[0];
        Player player = (Player) args[1];
        boolean asOp = (boolean) args[2];
        String command = (String) args[3];
        long delay = (long) args[4];
        if(asConsole) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            return false;
        }
        if(player == null) return false;
        if(asOp) player.setOp(true);
        if(delay > 0) Bukkit.getScheduler().runTaskLater((Plugin) action.getCore(), () -> Bukkit.dispatchCommand(player, command), delay);
        else {
            Bukkit.dispatchCommand(player, command);
        }
        if(asOp) player.setOp(false);
        return false;
    }
}
