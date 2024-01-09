package dev.wuason.mechanics.actions.executators;

import dev.wuason.mechanics.invmechanic.types.InvCustom;

import java.util.HashMap;

public class Executors {
    public static final HashMap<String, Executor> executors = new HashMap<>();

    static {

        executors.put("INVENTORY_CUSTOM", new Executor("INVENTORY_CUSTOM", InvCustom.class) {
            @Override
            public void registerPlaceholders(HashMap<String, Object> actualPlaceholders, Object... args) {
                InvCustom invCustom = (InvCustom) args[0];

                actualPlaceholders.put("$inventory$", invCustom.getInventory());

            }
        });
    }

}
