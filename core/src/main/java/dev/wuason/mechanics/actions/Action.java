package dev.wuason.mechanics.actions;

import bsh.EvalError;
import bsh.Interpreter;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.ArgumentProperties;
import dev.wuason.mechanics.actions.args.Arguments;
import dev.wuason.mechanics.actions.config.*;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class Action {
    private final Interpreter interpreter;
    private final MechanicAddon core;
    private final ActionManager actionManager;
    private final HashMap<String, Object> placeholders = new HashMap<>();
    private final HashMap<String, Object> placeholderReplacements = new HashMap<>();
    private final WeakHashMap<String, Argument> conditionArgumentsRegistered = new WeakHashMap<>();
    private final UUID id = UUID.randomUUID();
    private final Object[] args;
    private final ActionConfig actionConfig;
    private final String namespace;
    private final EventAction eventAction;


    private AtomicBoolean active = new AtomicBoolean(false);
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

    public Action load(@NotNull Run loadType){

        Runnable runnable = () -> {

            eventAction.registerPlaceholders(Action.this); // REGISTER EVENT ACTION PLACEHOLDERS
            actionConfig.getExecutor().registerPlaceholders(Action.this); // REGISTER EXECUTOR PLACEHOLDERS
            runCode(actionConfig.getImportsLine()); // LOAD IMPORTS



            //******* LOAD VAR LIST *******//

            for(VarListConfig<? extends Class<? extends Argument>> varListConfig : actionConfig.getVarsList()){

                List<Object> objList = new ArrayList<>();

                for(ArgumentConfig argumentConfig : varListConfig.getArguments()){

                    String argContent = argumentConfig.getArgument();

                    ArgumentProperties properties = Arguments.getArgumentProperties(argumentConfig.getType());

                    if(properties.isReSearchPlaceholders()){
                        argContent = ArgumentUtils.processArg(argContent, this);
                        argContent = ArgumentUtils.processArgSearchArgs(argContent, this);
                    }

                    Argument argument = Arguments.createArgument(argumentConfig.getType(), argContent);

                    objList.add(argument.computeArgInit(this));

                }

                if(varListConfig.getVar().contains("{") && varListConfig.getVar().contains("}")){
                    actionManager.setValueGlobalVar(namespace, varListConfig.getVar(), objList);
                    continue;
                }

                if(varListConfig.getVar().contains("%")){
                    registerPlaceholderReplacement(varListConfig.getVar(), objList);
                    continue;
                }
                registerPlaceholder(varListConfig.getVar(), objList);

            }

            //******* LOAD VARS *******//

            for(VarConfig varConfig : actionConfig.getVars()){

                String argContent = varConfig.getArgument().getArgument();

                ArgumentProperties properties = Arguments.getArgumentProperties(varConfig.getArgument().getType());

                if(properties.isReSearchPlaceholders()){
                    argContent = ArgumentUtils.processArg(argContent, this);
                    argContent = ArgumentUtils.processArgSearchArgs(argContent, this);
                }

                Argument argument = Arguments.createArgument(varConfig.getArgument().getType(), argContent);

                if(varConfig.getVar().contains("{") && varConfig.getVar().contains("}")){
                    actionManager.setValueGlobalVar(namespace, varConfig.getVar(), argument.computeArgInit(this));
                    continue;
                }
                if(varConfig.getVar().contains("%")){
                    registerPlaceholderReplacement(varConfig.getVar(), argument.computeArgInit(this));
                    continue;
                }
                registerPlaceholder(varConfig.getVar(), argument.computeArgInit(this));
            }

            //******* LOAD CONDITIONS *******//

            for(ConditionConfig conditionConfig : actionConfig.getConditions()){ // LOAD CONDITIONS
                loadCondition(conditionConfig);
            }

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


    /**
     * Executes the action. If the action is not loaded, it sets a flag to indicate that it should be executed when loaded.
     * If the action is loaded, it sets a flag to indicate that it is active and calls the execute method to perform the action.
     */
    public void run(){
        if(!loaded.get()) {
            pendingToRun.set(true);
            return;
        }
        active.set(true);
        execute(0);
    }

    /**
     * Executes a function with the specified configuration, run type, and conditions.
     *
     * @param functionConfig The configuration of the function to execute.
     * @param runType The type of execution (SYNC, ASYNC, CURRENT).
     * @param conditions The list of conditions to check before executing the function.
     */
    public void execute(FunctionConfig functionConfig, Run runType, List<ConditionConfig> conditions){
        if(runType == Run.CURRENT && pendingToRun.get()) runType = Run.SYNC;
        switch (runType){
            case SYNC -> {
                Bukkit.getScheduler().runTask((Plugin)core, () -> {
                    if(executeFunction(functionConfig, conditions));
                });
            }
            case ASYNC -> {
                Bukkit.getScheduler().runTaskAsynchronously((Plugin)core, () ->{
                    if(executeFunction(functionConfig, conditions));
                });
            }
            case CURRENT -> {
                if(executeFunction(functionConfig, conditions));
            }
        }
    }

    /**
     * Executes a function with the given function configuration and conditions.
     *
     * @param functionConfig The function configuration containing the function and arguments.
     * @param conditions     The conditions to check before executing the function.
     *                       Can be null if there are no conditions.
     * @return true if the function is executed successfully, false otherwise.
     */
    public boolean executeFunction(@NotNull FunctionConfig functionConfig, @Nullable List<ConditionConfig> conditions){
        if(conditions != null && !checkAllConditions(conditions)) return false;

        Function function = functionConfig.getFunction();

        Object[] argsComputed = new Object[function.getArgs().size()];

        FunctionArgument[] functionArguments = function.getOrderedArgs();

        for(int i=0;i<functionArguments.length;i++){

            FunctionArgument functionArgument = functionArguments[i];

            String argContent = functionConfig.getArgs().get(functionArgument.getName());

            if(argContent != null){
                if(function.getProperties().isProcessArgs() && functionArgument.getProperties().isProcessArg()) argContent = ArgumentUtils.processArg(argContent, this);
                if(function.getProperties().isProcessArgsSearchArgs() && functionArgument.getProperties().isProcessArgSearchArgs()) argContent = ArgumentUtils.processArgSearchArgs(argContent, this);
            }

            argsComputed[i] = functionArgument.computeArgInit(argContent, this, argsComputed);

        }
        return function.execute(this, argsComputed);

    }

    /**
     * Executes a function based on the provided function index and run type.
     *
     * @param functionIndex The index of the function to execute.
     * @param runType The type of run to perform (SYNC, ASYNC, CURRENT).
     */
    public void execute(int functionIndex, Run runType){
        if(!active.get() || actionConfig.getFunctions().size()<=functionIndex || functionIndex < 0) {
            finish();
            return;
        }

        actualFunction.set(functionIndex);

        FunctionConfig functionConfig = actionConfig.getFunctions().get(functionIndex);

        if(runType == Run.CURRENT && pendingToRun.get()) runType = Run.SYNC;

        switch (runType){
            case SYNC -> {
                Bukkit.getScheduler().runTask((Plugin)core, () -> {
                    if(executeFunction(functionConfig, actionConfig.getConditions())) return;
                    executeNext();
                });
            }
            case ASYNC -> {
                Bukkit.getScheduler().runTaskAsynchronously((Plugin)core, () -> {
                    if(executeFunction(functionConfig, actionConfig.getConditions())) return;
                    executeNext();
                });
            }
            case CURRENT -> {
                if(executeFunction(functionConfig, actionConfig.getConditions())) return;
                executeNext();
            }
        }
    }

    /**
     * Executes the specified function with the given index using the default run type.
     *
     * @param function The index of the function to execute.
     */
    public void execute(int function){
        execute(function, actionConfig.getRunType());
    }

    /**
     * Executes the next function in the Action using the specified Run type.
     *
     * @param runType the Run type to use for execution (SYNC, ASYNC, CURRENT)
     */
    public void executeNext(Run runType){
        execute(actualFunction.get() + 1, runType);
    }

    /**
     * Executes the next function in the Action using the specified run type.
     * If the Action is not active or there are no more functions to execute, it finishes.
     */
    public void executeNext(){
        executeNext(actionConfig.getRunType());
    }

    /**
     * Finishes the execution of the action.
     * Sets the 'active' flag of the action to false,
     * sets the 'actualFunction' value to -1,
     * removes the action from the action manager,
     * and prints "FINISH" to the console.
     */
    public void finish(){
        active.set(false);
        actualFunction.set(-1);
        actionManager.removeAction(id);
        System.out.println("FINISH");
    }

    /**
     * Executes the given code using an interpreter and returns the result.
     *
     * @param code The code to be executed.
     * @return The result of the code execution.
     */
    public Object runCode(String code){
        try {
            return interpreter.eval(code);
        } catch (EvalError e) {
        }
        return null;
    }

    //*********** CONDITIONS ***********//

    public boolean checkConditionWithLoad(ConditionConfig conditionConfig){
        String condition = conditionConfig.getReplacement();
        loadCondition(conditionConfig);
        return runCode(condition).equals(true);
    }

    public void loadCondition(ConditionConfig conditionConfig){
        for(Map.Entry<String, ArgumentConfig> entry : conditionConfig.getReplacements().entrySet()){
            String argContent = entry.getValue().getArgument();
            argContent = ArgumentUtils.processArg(argContent, this);
            argContent = ArgumentUtils.processArgSearchArgs(argContent, this);
            Argument argument = Arguments.createArgument(entry.getValue().getType(), argContent);
            registerPlaceholder(entry.getKey(), argument.computeArgInit(this));
            conditionArgumentsRegistered.put(entry.getKey(), argument);
        }
    }

    public boolean checkConditionWithOutLoad(ConditionConfig conditionConfig){
        String condition = conditionConfig.getReplacement();
        reLoadConditions();
        Object obj = runCode(condition);
        if(obj == null) return false;
        return obj.equals(true);
    }

    public void reLoadConditions(){
        for(Map.Entry<String, Argument> entry : conditionArgumentsRegistered.entrySet()){
            registerPlaceholder(entry.getKey(), entry.getValue().computeArgInit(this));
        }
    }

    public boolean checkAllConditions(List<ConditionConfig> list){
        for(ConditionConfig conditionConfig : list){
            if(!checkConditionWithOutLoad(conditionConfig)) return false;
        }
        return true;
    }



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
        return placeholders.getOrDefault(placeholder.toUpperCase(Locale.ENGLISH).intern(), "INVALID");
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
        runCode(methodEval);
    }


    public void registerDefaultMethods(){
        runCode("import dev.wuason.mechanics.actions.utils.ArgUtils; Object runArg(String argType, String value){ return ArgUtils.runArg($ACTION$, argType, value); }");
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
        return active.get();
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
