package dev.wuason.mechanics.actions.args.def.internal.functions.def.math;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternal;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternalArgument;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternalArgumentProperties;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternalProperties;
import dev.wuason.mechanics.actions.config.FunctionInternalConfig;
import dev.wuason.mechanics.utils.MathUtils;

import java.util.HashMap;

public class RandomNumber extends FunctionInternal {

    public static final String ID = "randomNumber";

    public static final FunctionInternalProperties PROPERTIES = new FunctionInternalProperties.Builder().build();

    public static final HashMap<String, FunctionInternalArgument> ARGUMENTS = new HashMap<String, FunctionInternalArgument>(){{

        FunctionInternalArgument min = new FunctionInternalArgument("min", 0, new FunctionInternalArgumentProperties.Builder().build()) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {
                try {
                    return Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    return 1;
                }
            }
        };

        FunctionInternalArgument max = new FunctionInternalArgument("max", 1, new FunctionInternalArgumentProperties.Builder().build()) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {
                try {
                    return Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    return 10;
                }
            }
        };

        put(min.getName(), min);
        put(max.getName(), max);

    }};



    public RandomNumber() {
        super(PROPERTIES, ARGUMENTS, "randomNumber");
    }

    @Override
    public Object compute(Action action, Object... args) {
        return MathUtils.randomNumber((int)args[0],(int)args[1]);
    }
}
