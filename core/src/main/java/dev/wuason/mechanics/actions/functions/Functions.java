package dev.wuason.mechanics.actions.functions;

import dev.wuason.mechanics.actions.functions.def.actions.ExecuteFunctions;
import dev.wuason.mechanics.actions.functions.def.actions.JavaCodeFunction;
import dev.wuason.mechanics.actions.functions.def.actions.WaitFunction;
import dev.wuason.mechanics.actions.functions.def.debug.DebugFunction;
import dev.wuason.mechanics.actions.functions.def.vanilla.ExecuteCommand;

import java.util.HashMap;
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

    }

    public static void registerFunction(Consumer<Function.Builder> functionBuilder){
        Function.Builder builder = new Function.Builder();
        functionBuilder.accept(builder);
        Function function = builder.build();
        FUNCTIONS.put(function.getName(), function);
    }
}
