package dev.wuason.mechanics.actions.executators;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.executators.def.DefaultExecutor;
import dev.wuason.mechanics.invmechanic.types.InvCustom;

import java.util.HashMap;
import java.util.function.Consumer;

public class Executors {
    public static final HashMap<String, Executor> EXECUTORS = new HashMap<>();

    static {



        /*EXECUTORS.put("INVENTORY_CUSTOM", new Executor("INVENTORY_CUSTOM", InvCustom.class) {
            @Override
            public void registerPlaceholders(Action action) {
                InvCustom invCustom = (InvCustom) action.getArgs()[0];
                action.registerPlaceholder("$inventory$", invCustom.getInventory());
                action.registerPlaceholder("$inventoryCustom$", invCustom);
            }
        });*/

        registerExecutor(builder -> {
            builder.setId("INVENTORY_CUSTOM");
            builder.setExecutorClass(InvCustom.class);
            builder.setRegisterPlaceholders(action -> {
                InvCustom invCustom = (InvCustom) action.getArgs()[0];
                action.registerPlaceholder("$inventory$", invCustom.getInventory());
                action.registerPlaceholder("$inventoryCustom$", invCustom);
            });
        });

        EXECUTORS.put("DEFAULT", new DefaultExecutor("DEFAULT"));

    }

    public static void registerExecutor(Consumer<Executor.Builder> executorBuilder) {
        Executor.Builder builder = new Executor.Builder();
        executorBuilder.accept(builder);
        Executor executor = builder.build();
        EXECUTORS.put(executor.getId(), executor);
    }

}
