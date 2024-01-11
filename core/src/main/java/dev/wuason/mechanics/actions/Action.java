package dev.wuason.mechanics.actions;

import bsh.EvalError;
import bsh.Interpreter;
import dev.wuason.mechanics.actions.config.ActionConfig;
import dev.wuason.mechanics.actions.config.FunctionConfig;
import dev.wuason.mechanics.actions.events.EventAction;
import dev.wuason.mechanics.actions.executators.Run;
import dev.wuason.mechanics.actions.functions.Function;
import dev.wuason.mechanics.actions.functions.FunctionArgument;
import dev.wuason.mechanics.actions.utils.ArgumentUtils;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class Action {
    private final Interpreter interpreter;
    private final MechanicAddon core;
    private final ActionManager actionManager;
    private final HashMap<String, Object> placeholders = new HashMap<>();
    private final HashMap<String, Object> placeholderReplacements = new HashMap<>();
    private final UUID id = UUID.randomUUID();
    private final Object[] args;
    private final ActionConfig actionConfig;
    private final String namespace;
    private final EventAction eventAction;


    private boolean active = false;
    private AtomicBoolean pendingToRun = new AtomicBoolean(false);
    private AtomicBoolean loaded = new AtomicBoolean(false);
    private AtomicInteger actualFunction = new AtomicInteger(-1);


    public Action(@NotNull MechanicAddon core, @Nullable HashMap<String, Object> initPlaceholders, @NotNull ActionManager actionManager, @NotNull ActionConfig actionConfig, @NotNull String namespace, @NotNull EventAction eventAction, @Nullable Object... args){
        if(!(core instanceof MechanicAddon)) throw new RuntimeException("Core must be a plugin");
        if(core == null) throw new RuntimeException("Core cannot be null");
        if(actionManager == null) throw new RuntimeException("ActionManager cannot be null");
        if(actionConfig == null) throw new RuntimeException("ActionConfig cannot be null");
        if(namespace == null) throw new RuntimeException("Namespace cannot be null");
        if(eventAction == null) throw new RuntimeException("EventAction cannot be null");
        if(args == null) args = new Object[0];
        if(initPlaceholders == null) initPlaceholders = new HashMap<>();

        this.core = core;
        this.actionManager = actionManager;
        this.namespace = namespace;
        this.args = args;
        this.actionConfig = actionConfig;
        this.eventAction = eventAction;

        this.interpreter = new Interpreter(); //BEANSHELL

        registerPlaceholders(initPlaceholders);
        registerDefaultMethods();
    }

    public Action load(Run loadType){

        Runnable runnable = () -> {

            eventAction.registerPlaceholders(Action.this);
            actionConfig.getExecutor().registerPlaceholders(Action.this);

            loaded.set(true);
            if(pendingToRun.get()) run();
        };

        switch (loadType){
            case SYNC -> {
                Bukkit.getScheduler().runTask((Plugin)core, runnable);
            }
            case ASYNC -> {
                Bukkit.getScheduler().runTaskAsynchronously((Plugin)core, runnable);
            }
            case CURRENT -> {
                runnable.run();
            }
        }

        return this;
    }

    public Action load(){
        return load(actionConfig.getRunType());
    }


    //*********** EXECUTION ***********//


    public void run(){
        if(!loaded.get()) {
            pendingToRun.set(true);
            return;
        }
        active = true;


    }

    public boolean execute(FunctionConfig functionConfig, Run runType){
        Function function = functionConfig.getFunction();

        Object[] argsComputed = new Object[function.getArgs().size()];

        FunctionArgument[] functionArguments = function.orderArgs(functionConfig.getArgs());

        for(int i=0;i<functionArguments.length;i++){
            FunctionArgument functionArgument = functionArguments[i];
            if(functionArgument == null) continue;
            String argContent = functionConfig.getArgs().get(functionArgument.getName());
            if(argContent == null) continue;
            if(function.getProperties().isProcessArgs() && functionArgument.getProperties().isProcessArg()) argContent = ArgumentUtils.processArg(argContent, this);
            if(function.getProperties().isProcessArgsSearchArgs() && functionArgument.getProperties().isProcessArgSearchArgs()) argContent = ArgumentUtils.processArgSearchArgs(argContent, this);

            argsComputed[i] = functionArgument.computeArg(argContent, this, argsComputed);

        }
        return function.execute(this, argsComputed);
    }

    public void execute(int functionIndex, Run runType){
        if(!active || actionConfig.getFunctions().size()<functionIndex || functionIndex < 0) return;
        actualFunction.set(functionIndex);
        FunctionConfig functionConfig = actionConfig.getFunctions().get(functionIndex);
        if(execute(functionConfig,runType)) return;
        execute(functionIndex + 1, runType);
    }

    public void execute(int function){
        execute(function, actionConfig.getRunType());
    }

    public void executeNext(Run runType){
        execute(actualFunction.get() + 1, runType);
    }

    public void executeNext(){
        executeNext(actionConfig.getRunType());
    }

    public void finish(){
        active = false;
        actualFunction.set(-1);
        actionManager.removeAction(id);
    }

    //*********** CONDITIONS ***********//



    //*********** PLACEHOLDERS ***********//

    public void unRegisterPlaceholder(@NotNull String placeholder){
        placeholders.remove(placeholder.toUpperCase(Locale.ENGLISH).intern());
        try {
            interpreter.unset(placeholder.toUpperCase(Locale.ENGLISH).intern());
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
        placeholders.put(placeholder.toUpperCase(Locale.ENGLISH).intern(),value);
        try {
            interpreter.set(placeholder.toUpperCase(Locale.ENGLISH).intern(),value);
        } catch (EvalError e)
        {
        }
    }

    public void registerPlaceholderReplacement(@NotNull String placeholder, @NotNull Object value){
        placeholderReplacements.put(placeholder.toUpperCase(Locale.ENGLISH).intern(),value);
    }

    public void unRegisterPlaceholderReplacement(@NotNull String placeholder){
        placeholderReplacements.remove(placeholder.toUpperCase(Locale.ENGLISH).intern());
    }

    public void unRegisterAllPlaceholderReplacements(){
        placeholderReplacements.clear();
    }

    public void registerPlaceholderReplacements(@NotNull HashMap<String, Object> placeholders){
        for(Map.Entry<String, Object> placeholderEntry : placeholders.entrySet()){
            registerPlaceholderReplacement(placeholderEntry.getKey(), placeholderEntry.getValue());
        }
    }

    public Object getPlaceholderReplacement(@NotNull String placeholder){
        return placeholderReplacements.get(placeholder.toUpperCase(Locale.ENGLISH).intern());
    }

    public boolean hasPlaceholderReplacement(@NotNull String placeholder){
        return placeholderReplacements.containsKey(placeholder.toUpperCase(Locale.ENGLISH).intern());
    }

    public Object getPlaceholder(@NotNull String placeholder){
        return placeholders.get(placeholder.toUpperCase(Locale.ENGLISH).intern());
    }

    public boolean hasPlaceholder(@NotNull String placeholder){
        return placeholders.containsKey(placeholder.toUpperCase(Locale.ENGLISH).intern());
    }

    public void registerDefPlaceholders(){
        registerPlaceholder("$action$", this);
        registerPlaceholder("$actionConfig$", actionConfig);
        registerPlaceholder("$eventAction$", actionConfig.getEventAction());
        registerPlaceholder("$executor$", actionConfig.getExecutor());
        registerPlaceholder("$namespace$", namespace);
        registerPlaceholder("$args$", args);
        registerPlaceholderReplacement("%slash_char%", "/");
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


    public HashMap<String, Object> getPlaceholders() {
        return placeholders;
    }

    public UUID getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public Object[] getArgs() {
        return args;
    }

    public ActionConfig getActionConfig() {
        return actionConfig;
    }

    public String getNamespace() {
        return namespace;
    }

    public EventAction getEventAction() {
        return eventAction;
    }


}
