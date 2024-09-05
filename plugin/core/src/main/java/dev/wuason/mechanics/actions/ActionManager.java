package dev.wuason.mechanics.actions;

import dev.wuason.mechanics.actions.api.events.CallEventApiEvent;
import dev.wuason.mechanics.actions.config.ActionConfig;
import dev.wuason.mechanics.actions.events.EventAction;
import dev.wuason.mechanics.actions.events.Events;
import dev.wuason.mechanics.actions.vars.GlobalVar;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class ActionManager {

    private HashMap<UUID, Action> actionsRegistered = new HashMap<>();
    private HashMap<String, HashMap<String, GlobalVar>> globalVars = new HashMap<>();
    private MechanicAddon core;
    private boolean listenDefEvents = false;
    private List<Class<? extends EventAction>> listenEvents = new ArrayList<>();
    private ActionConfigManager actionConfigManager;

    //******** EVENTS API ********//
    public List<Consumer<CallEventApiEvent>> callEventApiEventListeners = new ArrayList<>();

    //******** CONFIG ********//

    private final HashMap<String, ActionConfig> actionConfigs = new HashMap<>();
    private final HashMap<String, ArrayList<ActionConfig>> eventActionConfigs = new HashMap<>();


    //******** CONSTRUCTORS ********//

    public ActionManager(MechanicAddon core, boolean actionConfigManager) {
        if(!(core instanceof Plugin)) throw new RuntimeException("Core must be a plugin");
        this.core = core;

        Bukkit.getPluginManager().registerEvents(new Events((Plugin) core, this), (Plugin) core);

        if(actionConfigManager) this.actionConfigManager = new ActionConfigManager(core, this);

        registerAllEvents();
    }

    //******** EVENTS API METHODS ********//

    public void addCallEventApiEventListener(Consumer<CallEventApiEvent> listener){
        callEventApiEventListeners.add(listener);
    }

    public void removeCallEventApiEventListener(Consumer<CallEventApiEvent> listener){
        callEventApiEventListeners.remove(listener);
    }

    public void callEventApiEvent(CallEventApiEvent event){
        callEventApiEventListeners.forEach(listener -> listener.accept(event));
    }

    //******** ACTIONS ********//

    /**
     * Retrieves an Action object with the specified UUID.
     *
     * @param id the UUID of the Action
     * @return the Action object with the specified UUID, or null if no such Action exists
     */
    public Action getAction(UUID id){
        return actionsRegistered.getOrDefault(id,null);
    }

    /**
     * Creates a new Action based on the given parameters.
     *
     * @param actionConfig The ActionConfig object that defines the action's configuration. Must not be null.
     * @param placeholders A HashMap of placeholders to be used in the action. Can be null.
     * @param namespace The namespace for the action. Must not be null.
     * @param eventAction The EventAction type for the action. Must not be null.
     * @param args Additional arguments for the action. Can be null.
     * @return The newly created Action object.
     * @throws RuntimeException if actionConfig is null.
     */
    public Action createAction(@NotNull ActionConfig actionConfig, @Nullable HashMap<String, Object> placeholders, @NotNull String namespace, @NotNull EventAction eventAction, @Nullable Object... args){
        if(actionConfig == null) throw new RuntimeException("ActionConfig cannot be null");
        if(args == null) args = new Object[0];
        if(placeholders == null) placeholders = new HashMap<>();
        if(!globalVars.containsKey(namespace)) globalVars.put(namespace,new HashMap<>());
        Action action = new Action(core, placeholders, this, actionConfig, namespace, eventAction, args);
        actionsRegistered.put(action.getId(), action);
        return action;
    }


    /**
     * Creates an Action based on the given parameters.
     *
     * @param actionConfigId  The ID of the ActionConfig object that defines the action's configuration. Must not be null.
     * @param placeholders    A HashMap of placeholders to be used in the action. Can be null.
     * @param namespace       The namespace for the action. Must not be null.
     * @param eventAction     The EventAction type for the action. Must not be null.
     * @param args            Additional arguments for the action. Can be null.
     * @return The newly created Action object.
     * @throws RuntimeException if actionConfigId is not found in actionConfigs.
     */
    public Action createAction(@NotNull String actionConfigId, @Nullable HashMap<String, Object> placeholders, @NotNull String namespace, @NotNull EventAction eventAction, @Nullable Object... args){
        ActionConfig actionConfig = actionConfigs.getOrDefault(actionConfigId,null);
        if(actionConfig == null) throw new RuntimeException("ActionConfig with id " + actionConfigId + " not found");
        return createAction(actionConfig, placeholders, namespace, eventAction, args);
    }

    /**
     * Removes an action from the registered actions.
     *
     * @param id the UUID of the action to be removed
     */
    public void removeAction(UUID id){
        actionsRegistered.remove(id);
    }

    /**
     * Forcefully stops the action with the specified ID.
     *
     * @param id The ID of the action to be stopped.
     */
    public void forceStopAction(UUID id){
        Action action = actionsRegistered.getOrDefault(id,null);
        if(action != null) action.finish();
    }





    //******** GLOBAL VARS ********//
    public GlobalVar getGlobalVar(String namespace, String id){
        return globalVars.getOrDefault(namespace,new HashMap<>()).getOrDefault(id.toUpperCase(Locale.ENGLISH),null);
    }

    public void setValueGlobalVar(String namespace, String id, Object value){
        HashMap<String, GlobalVar> globalVars = this.globalVars.getOrDefault(namespace,new HashMap<>());
        globalVars.put(id.toUpperCase(Locale.ENGLISH),new GlobalVar(id.toUpperCase(Locale.ENGLISH), value));
        if(!this.globalVars.containsKey(namespace)) this.globalVars.put(namespace,globalVars);
    }

    public void removeGlobalVar(String namespace, String id){
        HashMap<String, GlobalVar> globalVars = this.globalVars.getOrDefault(namespace,new HashMap<>());
        globalVars.remove(id.toUpperCase(Locale.ENGLISH));
    }

    public boolean isGlobalVarRegistered(String namespace, String id){
        return globalVars.containsKey(namespace) && globalVars.get(namespace).containsKey(id.toUpperCase(Locale.ENGLISH));
    }

    public void clearGlobalVars(String namespace){
        globalVars.remove(namespace);
    }

    //******** ACTIONS CONFIGS ********//

    public void registerActionConfig(ActionConfig actionConfig){
        actionConfigs.put(actionConfig.getId(),actionConfig);
        ArrayList<ActionConfig> eventActionConfigs = this.eventActionConfigs.getOrDefault(actionConfig.getEventAction(), new ArrayList<>());
        eventActionConfigs.add(actionConfig);
    }

    public void unRegisterActionConfig(String id){
        ActionConfig actionConfig = actionConfigs.getOrDefault(id,null);
        if(actionConfig == null) return;
        ArrayList<ActionConfig> eventActionConfigs = this.eventActionConfigs.getOrDefault(actionConfig.getEventAction(), new ArrayList<>());
        eventActionConfigs.remove(actionConfig);
        actionConfigs.remove(id);

    }

    public boolean isActionConfigRegistered(String id){
        return actionConfigs.containsKey(id);
    }

    public ActionConfig getActionConfig(String id){
        return actionConfigs.getOrDefault(id,null);
    }

    public Collection<ActionConfig> getActionConfigs(){
        return actionConfigs.values();
    }

    public ArrayList<ActionConfig> getEventActionConfigs(String eventAction){
        return eventActionConfigs.getOrDefault(eventAction, new ArrayList<>());
    }

    public void clearActionConfigs(){
        actionConfigs.clear();
    }

    public ActionConfigManager getActionConfigManager() {
        return actionConfigManager;
    }

    //******** EVENTS ********//

    public void callEvent(EventAction eventAction, String namespace, Object... args){
        if(!eventActionConfigs.containsKey(eventAction.getId())) return;
        CallEventApiEvent event = new CallEventApiEvent(eventAction, namespace, args);
        for(Consumer<CallEventApiEvent> listener : callEventApiEventListeners){
            listener.accept(event);
            if(event.isCancelled()) return;
        }
        for(ActionConfig actionConfig : eventActionConfigs.get(eventAction.getId())){

            Action action = createAction(actionConfig, null, namespace, eventAction, args);
            action.load().run();

        }
    }

    public void registerAllEvents(){
        eventActionConfigs.clear();
        for(Map.Entry<String, Class<? extends EventAction>> entry : Events.EVENTS.entrySet()){
            eventActionConfigs.put(entry.getKey(), new ArrayList<>());
        }
    }

    //******** LISTEN DEF EVENTS ********//

    public boolean isListenDefEvents() {
        return listenDefEvents;
    }

    public void setListenDefEvents(boolean listenDefEvents) {
        this.listenDefEvents = listenDefEvents;
    }

    public boolean isListenEvent(Class<? extends EventAction> eventAction){
        return listenEvents.contains(eventAction);
    }

    public void setListenEvent(Class<? extends EventAction> eventAction){
        if(!listenEvents.contains(eventAction)) listenEvents.add(eventAction);
    }

    public void unSetListenEvent(Class<? extends EventAction> eventAction){
        if(listenEvents.contains(eventAction)) listenEvents.remove(eventAction);
    }

    public void clearListenEvents(){
        listenEvents.clear();
    }


}
