package dev.wuason.mechanics.actions.args.def.internal.functions.def.math;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternal;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternalArgument;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternalArgumentProperties;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternalProperties;
import dev.wuason.mechanics.utils.MathUtils;

import java.util.HashMap;

public class Chance extends FunctionInternal {

    public static final String ID = "chance";

    public static final FunctionInternalProperties PROPERTIES = new FunctionInternalProperties.Builder().build();

    public static final HashMap<String, FunctionInternalArgument> ARGUMENTS = new HashMap<String, FunctionInternalArgument>(){{

        FunctionInternalArgument arg0 = new FunctionInternalArgument("chance", 0, new FunctionInternalArgumentProperties.Builder().build()) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {
                try {
                    return Float.parseFloat(line);
                } catch (Exception e) {
                    return 100;
                }
            }
        };

        put(arg0.getName(), arg0);

    }};


    public Chance() {
        super(PROPERTIES, ARGUMENTS, "chance");
    }

    @Override
    public Object compute(Action action, Object... args) {
        return MathUtils.chance((Float) args[0]);
    }
}
