package dev.wuason.mechanics.actions;

import bsh.EvalError;
import bsh.Interpreter;
import dev.wuason.mechanics.actions.events.EventAction;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Action {
    private final Interpreter interpreter;
    private final MechanicAddon core;
    private final ActionManager actionManager;
    private final EventAction event;
    private final HashMap<String, Object> placeholders = new HashMap<>();

    private boolean active = false;


    public Action(@NotNull MechanicAddon core, @Nullable HashMap<String, Object> initPlaceholders, @NotNull ActionManager actionManager, @Nullable EventAction event){
        if(!(core instanceof MechanicAddon)) throw new RuntimeException("Core must be a plugin");
        if(core == null) throw new RuntimeException("Core cannot be null");
        if(actionManager == null) throw new RuntimeException("ActionManager cannot be null");
        if(initPlaceholders == null) initPlaceholders = new HashMap<>();

        this.core = core;
        this.actionManager = actionManager;
        this.event = event;
        this.interpreter = new Interpreter();

        registerPlaceholders(initPlaceholders);
        registerDefaultMethods();
    }

    public void load(){

    }

    public void finish(){
        active = false;
        actionManager.
    }


    //*********** PLACEHOLDERS ***********//

    public void unRegisterPlaceholder(@NotNull String placeholder){
        placeholders.remove(placeholder.intern());
        try {
            interpreter.unset(placeholder.intern());
        } catch (EvalError e)
        {
        }
    }

    public void unRegisterAllPlaceholders(){
        for(Map.Entry<String, Object> placeholderEntry : placeholders.entrySet()){
            unRegisterPlaceholder(placeholderEntry.getKey());
        }
    }

    public void registerPlaceholders(@NotNull HashMap<String, Object> placeholders){
        for(Map.Entry<String, Object> placeholderEntry : placeholders.entrySet()){
            registerPlaceholder(placeholderEntry.getKey(), placeholderEntry.getValue());
        }
    }

    public void registerPlaceholder(@NotNull String placeholder, @NotNull Object value){
        placeholders.put(placeholder.intern(),value);
        try {
            interpreter.set(placeholder.intern(),value);
        } catch (EvalError e)
        {
        }
    }

    public Object getPlaceholder(@NotNull String placeholder){
        return placeholders.get(placeholder.intern());
    }

    public boolean hasPlaceholder(@NotNull String placeholder){
        return placeholders.containsKey(placeholder.intern());
    }

    //*********** CUSTOM METHODS INTERPRETER ***********//

    public void registerMethod(String methodEval){
        try {
            interpreter.eval(methodEval);
        } catch (EvalError e) {
            throw new RuntimeException(e);
        }
    }

    public void registerDefaultMethods(){
        try {
            interpreter.eval("import dev.wuason.mechanics.actions.utils.ArgUtils; Object runArg(String argType, String value){ return ArgUtils.runArg($ACTION$, argType, value); }");
        } catch (EvalError e) {
            throw new RuntimeException(e);
        }
    }

    //*********** GETTERS ***********//

    public MechanicAddon getCore() {
        return core;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public EventAction getEvent() {
        return event;
    }

    public HashMap<String, Object> getPlaceholders() {
        return placeholders;
    }


}
