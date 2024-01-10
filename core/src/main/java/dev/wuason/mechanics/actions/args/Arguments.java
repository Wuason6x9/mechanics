package dev.wuason.mechanics.actions.args;

import dev.wuason.mechanics.actions.args.def.*;

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

    }
}
