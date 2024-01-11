package dev.wuason.mechanics.actions.functions.def.vanilla;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.functions.Function;
import dev.wuason.mechanics.actions.functions.FunctionArgument;
import dev.wuason.mechanics.utils.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class ExecuteCommand extends Function {

    private static final Collection<FunctionArgument> args = Collections.unmodifiableCollection(new ArrayList<>()
    {{

        add(new FunctionArgument("asConsole", 0) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {
                try {
                    return Boolean.parseBoolean(line);
                } catch (Exception e) {
                    return false;
                }
            }

        });

        add(new FunctionArgument("player", 1) {
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

        });

        add(new FunctionArgument("asOp", 2) {
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

        });

        add(new FunctionArgument("command", 3) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {

                String command = AdventureUtils.deserializeLegacy(line, (Player)args[1]);

                return command;
            }

        });

        add(new FunctionArgument("delay", 4) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {
                try {
                    return Long.parseLong(line);
                } catch (Exception e) {
                    return 0L;
                }
            }

        });


    }}
    );

    public ExecuteCommand() {
        super("executecommand", args);
    }

    @Override
    public boolean execute(Action action, Object... Args) {
        boolean asConsole = (boolean)Args[0];
        Player player = (Player)Args[1];
        boolean asOp = (boolean)Args[2];
        String command = (String)Args[3];
        long delay = (long)Args[4];
        if(asConsole) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            return true;
        }
        if(player == null) return true;
        if(asOp) player.setOp(true);
        if(delay > 0) Bukkit.getScheduler().runTaskLater((Plugin) action.getCore(), () -> Bukkit.dispatchCommand(player, command), delay);
        else {
            Bukkit.dispatchCommand(player, command);
        }
        if(asOp) player.setOp(false);
        return true;
    }
}
