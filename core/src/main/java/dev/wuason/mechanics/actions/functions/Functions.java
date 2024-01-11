package dev.wuason.mechanics.actions.functions;

import dev.wuason.mechanics.actions.functions.def.actions.WaitFunction;
import dev.wuason.mechanics.actions.functions.def.vanilla.ExecuteCommand;

import java.util.HashMap;

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



    }
}
