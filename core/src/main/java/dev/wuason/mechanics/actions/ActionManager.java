package dev.wuason.mechanics.actions;

import dev.wuason.mechanics.actions.config.ActionConfig;
import dev.wuason.mechanics.actions.vars.GlobalVar;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class ActionManager {

    private HashMap<UUID, Action> actionsRegistered = new HashMap<>();
    private HashMap<String, HashMap<String, GlobalVar>> globalVars = new HashMap<>();
    private MechanicAddon core;

    //******** CONFIG ********//

    private final HashMap<String, ActionConfig> actionConfigs = new HashMap<>();

    public ActionManager(MechanicAddon core) {
        if(!(core instanceof Plugin)) throw new RuntimeException("Core must be a plugin");
        this.core = core;
    }

    //******** ACTIONS ********//

    public Action getAction(UUID id){
        return actionsRegistered.getOrDefault(id,null);
    }

    public Action createAction(@NotNull ActionConfig actionConfig, @Nullable HashMap<String, Object> placeholders, String namespace, @Nullable Object... args){
        if(actionConfig == null) throw new RuntimeException("ActionConfig cannot be null");
        if(args == null) args = new Object[0];
        if(placeholders == null) placeholders = new HashMap<>();

        if(!globalVars.containsKey(namespace)) globalVars.put(namespace,new HashMap<>());

        Action action = new Action(core, placeholders, this, actionConfig, namespace, args);
        actionsRegistered.put(action.getId(), action);


        return action;
    }

    public void removeAction(UUID id){
        actionsRegistered.remove(id);
    }

    public void forceStopAction(UUID id){
        Action action = actionsRegistered.getOrDefault(id,null);
        if(action != null) action.finish();
    }





    //******** GLOBAL VARS ********//
    public GlobalVar getGlobalVar(String namespace, String id){
        return globalVars.getOrDefault(namespace,new HashMap<>()).getOrDefault(id,null);
    }

    public void setValueGlobalVar(String namespace, String id, Object value){
        HashMap<String, GlobalVar> globalVars = this.globalVars.getOrDefault(namespace,new HashMap<>());
        globalVars.put(id,new GlobalVar(id, value));
        if(!this.globalVars.containsKey(namespace)) this.globalVars.put(namespace,globalVars);
    }

    public void removeGlobalVar(String namespace, String id){
        HashMap<String, GlobalVar> globalVars = this.globalVars.getOrDefault(namespace,new HashMap<>());
        globalVars.remove(id);
    }

    public void clearGlobalVars(String namespace){
        globalVars.remove(namespace);
    }

    //******** ACTIONS CONFIGS ********//

    public void registerActionConfig(ActionConfig actionConfig){



    }

    public void unRegisterActionConfig(String id){



    }

    public ActionConfig getActionConfig(String id){
        return actionConfigs.getOrDefault(id,null);
    }

    public void clearActionConfigs(){
        actionConfigs.clear();
    }

}
