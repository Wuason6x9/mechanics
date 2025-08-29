package dev.wuason.mechanics.actions.args.def.internal.functions.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternal;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternalArgument;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternalArgumentProperties;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternalProperties;
import dev.wuason.mechanics.actions.utils.ActionConfigUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomObject extends FunctionInternal {
    public static final String ID = "randomObject";
    public static final HashMap<String, FunctionInternalArgument> ARGS = new HashMap<>(){{

        FunctionInternalArgument list = new FunctionInternalArgument("list", 0, new FunctionInternalArgumentProperties.Builder().build()){
            @Override
            public Object computeArg(String line, Action action, Object... args) {

                return ActionConfigUtils.getListFromArgProcess(line, action);

            }
        };

        put(list.getName(), list);

    }};

    public static final FunctionInternalProperties PROPERTIES = new FunctionInternalProperties.Builder().build();

    public RandomObject() {
        super(PROPERTIES, ARGS, "randomObject");
    }

    @Override
    public Object compute(Action action, Object... args) {
        List<?> list = (List<?>) args[0];
        Random random = new Random();
        int randomIndex = random.nextInt( list.size() );
        return list.get(randomIndex);
    }



}
