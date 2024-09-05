package dev.wuason.mechanics.actions.functions;

import dev.wuason.mechanics.actions.functions.def.actions.ExecuteAction;
import dev.wuason.mechanics.actions.functions.def.actions.ExecuteFunctions;
import dev.wuason.mechanics.actions.functions.def.actions.JavaCodeFunction;
import dev.wuason.mechanics.actions.functions.def.actions.WaitFunction;
import dev.wuason.mechanics.actions.functions.def.debug.DebugFunction;
import dev.wuason.mechanics.actions.functions.def.vanilla.CancelEventFunction;
import dev.wuason.mechanics.actions.functions.def.vanilla.ExecuteCommand;
import dev.wuason.mechanics.actions.functions.def.vanilla.PlaySound;
import dev.wuason.mechanics.actions.functions.def.vars.Vars;
import dev.wuason.mechanics.actions.utils.ActionConfigUtils;
import dev.wuason.mechanics.utils.AdventureUtils;
import dev.wuason.mechanics.utils.PlayerUtils;
import dev.wuason.nms.wrappers.NMSManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class Functions {
    public static final HashMap<String, Function> FUNCTIONS = new HashMap<>();

    static {

        //********** WAIT FUNCTION **********//

        Function waitFunction = new WaitFunction();
        FUNCTIONS.put(
                waitFunction.getName(),
                waitFunction
        );

        //********** EXECUTE COMMAND FUNCTION **********//

        Function executeCommandFunction = new ExecuteCommand();
        FUNCTIONS.put(
                executeCommandFunction.getName(),
                executeCommandFunction
        );

        //********** DEBUG FUNCTION **********//

        Function debugFunction = new DebugFunction();
        FUNCTIONS.put(
                debugFunction.getName(),
                debugFunction
        );

        //********** JAVA CODE FUNCTION **********//

        Function javaCodeFunction = new JavaCodeFunction();

        FUNCTIONS.put(
                javaCodeFunction.getName(),
                javaCodeFunction
        );

        //********** EXECUTE FUNCTIONS FUNCTION **********//

        Function executeFunctionsFunction = new ExecuteFunctions();

        FUNCTIONS.put(
                executeFunctionsFunction.getName(),
                executeFunctionsFunction
        );

        //********** ExecuteAction FUNCTION **********//

        Function executeActionFunction = new ExecuteAction();

        FUNCTIONS.put(
                executeActionFunction.getName(),
                executeActionFunction
        );

        //********** PlaySound FUNCTION **********//

        Function playSoundFunction = new PlaySound();

        FUNCTIONS.put(
                playSoundFunction.getName(),
                playSoundFunction
        );


        //********** VARS FUNCTION **********//
        Vars.register();

        //********** CANCEL FUNCTION **********//

        Function cancelFunction = new CancelEventFunction();

        FUNCTIONS.put(
                cancelFunction.getName(),
                cancelFunction
        );

        registerDefaultFunctions();
    }

    public static void registerFunction(Consumer<Function.Builder> functionBuilder){
        Function.Builder builder = new Function.Builder();
        functionBuilder.accept(builder);
        Function function = builder.build();
        FUNCTIONS.put(function.getName(), function);
    }

    public static void registerDefaultFunctions(){

        registerFunction(fBuilder -> {


            fBuilder.setName("sendSignGui");

            fBuilder.addArguments(argBuilder -> {

                argBuilder.addArgument(0 , "player", (s, action, objects) -> {

                    if(s == null) return action.getPlaceholder("$player$");
                    return PlayerUtils.getPlayer(s);

                }, pBuilder -> {

                    pBuilder.setRequired(false);
                    pBuilder.setAutoGetPlaceholder(true);

                });

            });

            fBuilder.addArguments(argBuilder -> {

                argBuilder.addArgument(1 , "var", (s, action, objects) -> {
                    if(!s.contains("$")) {
                        AdventureUtils.sendMessagePluginConsole(action.getCore(), "<red> ERROR: <yellow> The var must be a placeholder");
                        return null;
                    }
                    return s;

                }, pBuilder -> {

                    pBuilder.setRequired(true);
                    pBuilder.setAutoGetPlaceholder(false);
                    pBuilder.setProcessArgSearchArgs(false);
                    pBuilder.setProcessArg(false);

                });

            });

            fBuilder.addArguments(argBuilder -> {

                argBuilder.addArgument(2, "lines", (s, action, objects) -> {
                    if(s == null) return new ArrayList<>();
                    return ActionConfigUtils.getListFromArg(s);
                }, pBuilder -> {

                    pBuilder.setRequired(false);
                    pBuilder.setAutoGetPlaceholder(true);
                    pBuilder.setProcessArgSearchArgs(true);
                    pBuilder.setProcessArg(true);

                });

            });

            fBuilder.setExecute((action, objects) -> {

                Player player = (Player) objects[0];
                String var = (String) objects[1];
                String[] lines = new String[4];
                List<String> list = (List<String>) objects[2];
                for(int i = 0; i < 4; i++){
                    if(i>=list.size()) lines[i] = "";
                    else lines[i] = AdventureUtils.deserializeLegacy(list.get(i), player);
                }
                if(player == null) return false;
                if(var == null) return false;
                if(lines == null) return false;

                NMSManager.getVersionWrapper().openSing(player, lines, strings -> {

                    StringBuilder stringBuilder = new StringBuilder();

                    for (String string : strings) {
                        stringBuilder.append(string);
                    }

                    action.registerPlaceholder(var, stringBuilder.toString());

                    Bukkit.getScheduler().runTask((Plugin) action.getCore(), () -> {
                        player.closeInventory();
                        action.executeNext();
                    });

                });

                return true;

            });


        });

    }
}
