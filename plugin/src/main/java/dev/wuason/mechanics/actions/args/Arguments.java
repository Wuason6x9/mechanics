package dev.wuason.mechanics.actions.args;

import dev.wuason.mechanics.actions.args.def.*;
import dev.wuason.mechanics.actions.args.def.internal.InternalArg;
import dev.wuason.mechanics.actions.args.def.internal.PlayerArg;
import dev.wuason.mechanics.actions.config.ArgumentConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Arguments {
    public static final HashMap<String, Class<? extends Argument>> ARGUMENTS = new HashMap<>();

    static {

        ARGUMENTS.put("TEXT", TextArg.class);
        ARGUMENTS.put("NUMBER", NumberArg.class);
        ARGUMENTS.put("BOOLEAN", BooleanArg.class);
        ARGUMENTS.put("ADAPTER", AdapterArg.class);
        ARGUMENTS.put("VAR", VarArg.class);
        ARGUMENTS.put("INVENTORY", InventoryArg.class);
        ARGUMENTS.put("FLOAT", FloatArg.class);
        ARGUMENTS.put("JAVA", JavaArg.class);
        ARGUMENTS.put("INTERNAL", InternalArg.class);
        ARGUMENTS.put("EVENT", EventArg.class);
        ARGUMENTS.put("PLAYER", PlayerArg.class);

    }

    public static Argument createArgument(Class<? extends Argument> type, String line, Object... args){
        Object instance = null;
        try {
            instance = type.getConstructors()[0].newInstance(line, new Object[]{args});
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return (Argument) instance;
    }

    public static Argument createArgument(String type, String line, Object... args){
        if(!ARGUMENTS.containsKey(type)) throw new RuntimeException("Argument type " + type + " not found");
        return createArgument(ARGUMENTS.get(type), line, args);
    }

    public static Argument createArgument(String type, String line){
        return createArgument(type, line, new Object[0]);
    }

    public static Argument createArgument(Class<? extends Argument> type, String line){
        return createArgument(type, line, new Object[0]);
    }

    public static Argument createArgument(ArgumentConfig arg){
        return createArgument(arg.getType(), arg.getArgument());
    }

    public static Argument createArgument(ArgumentConfig arg, Object... args){
        return createArgument(arg.getType(), arg.getArgument(), args);
    }

    public static ArgumentProperties getArgumentProperties(String type){
        Argument argument = createArgument(type, "");
        return argument.getProperties();
    }

    public static ArgumentProperties getArgumentProperties(Class<? extends Argument> type){
        Argument argument = createArgument(type, "");
        return argument.getProperties();
    }

    public static ArgumentProperties getArgumentProperties(ArgumentConfig arg){
        Argument argument = createArgument(arg);
        return argument.getProperties();
    }
}
