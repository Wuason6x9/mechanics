package dev.wuason.mechanics.actions;

import dev.wuason.mechanics.actions.executators.Executor;
import dev.wuason.mechanics.actions.vars.GlobalVar;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class ActionManager {

    private HashMap<UUID, Action> actionsActive = new HashMap<>();
    private HashMap<String, HashMap<String, GlobalVar>> globalVars = new HashMap<>();
    private MechanicAddon core;

    public ActionManager(MechanicAddon core) {
        if(!(core instanceof Plugin)) throw new RuntimeException("Core must be a plugin");
        this.core = core;
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

}
