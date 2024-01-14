package dev.wuason.mechanics.actions.args.def.internal.functions;

import dev.wuason.mechanics.actions.args.def.internal.functions.def.RandomObject;
import dev.wuason.mechanics.actions.args.def.internal.functions.def.adapter.GetAdapterId;
import dev.wuason.mechanics.actions.args.def.internal.functions.def.adapter.GetItemStackByAdapter;
import dev.wuason.mechanics.actions.args.def.internal.functions.def.math.Chance;
import dev.wuason.mechanics.actions.args.def.internal.functions.def.math.RandomNumber;
import dev.wuason.mechanics.actions.config.FunctionInternalConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;

public class FunctionsInternal {
    public static HashMap<String, Class<? extends FunctionInternal>> FUNCTIONS = new HashMap<>();


    static {

        register(RandomObject.ID, RandomObject.class);
        register(Chance.ID, Chance.class);
        register(RandomNumber.ID, RandomNumber.class);
        register(GetItemStackByAdapter.ID, GetItemStackByAdapter.class);
        register(GetAdapterId.ID, GetAdapterId.class);

    }

    public static void register(String id, Class<? extends FunctionInternal> clazz){
        FUNCTIONS.put(id.toUpperCase(Locale.ENGLISH), clazz);
    }

    public static FunctionInternal createFunctionInternal(String id, FunctionInternalConfig config){
        Class<? extends FunctionInternal> clazz = FUNCTIONS.get(id.toUpperCase(Locale.ENGLISH));
        if(clazz == null) return null;
        try {
            return (FunctionInternal) clazz.getConstructors()[0].newInstance(config);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
