package dev.wuason.mechanics.actions;

import bsh.Interpreter;
import dev.wuason.mechanics.actions.events.EventAction;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class Action {
    private Interpreter interpreter;
    private MechanicAddon core;

    public Action(@NotNull MechanicAddon core, @Nullable HashMap<String, Object> initPlaceholders, @NotNull ActionManager actionManager, @NotNull EventAction event){

    }

    //*********** PLACEHOLDERS ***********//

    public void unRegisterPlaceholder(@NotNull String placeholder){

    }
    public void registerPlaceholder(@NotNull String placeholder, @NotNull Object value){

    }

    //*********** GETTERS ***********//

    public MechanicAddon getCore() {
        return core;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }
}
