package dev.wuason.mechanics.actions.functions.def.actions;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.config.ActionConfig;
import dev.wuason.mechanics.actions.events.def.ActionCreateEvent;
import dev.wuason.mechanics.actions.functions.Function;
import dev.wuason.mechanics.actions.functions.FunctionArgument;
import dev.wuason.mechanics.actions.functions.FunctionProperties;
import dev.wuason.mechanics.actions.utils.ActionConfigUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecuteAction extends Function {


    public ExecuteAction() {
        super("executeAction",
                new FunctionArgument.Builder()
                        .addArgument(0, "action", (s, action, objects) -> {
                            if(action.getActionManager().isActionConfigRegistered(s)) throw new RuntimeException("Action " + s + " not registered");
                            return action.getActionManager().getActionConfig(s);
                        })
                        .addArgument(1, "vars", (s, action, objects) -> {
                            List<String> listStringVars = new ArrayList<>();
                            if(s != null){
                                listStringVars.addAll(ActionConfigUtils.getListFromArg(s));
                            }

                            HashMap<String, Object> placeholders = new HashMap<>();

                            for(String v : listStringVars){
                                if(!action.hasPlaceholder(v)) continue;
                                placeholders.put(v, action.getPlaceholder(v));
                            }
                            return placeholders;
                        })
                        .addArgument(2, "namespace", (s, action, objects) -> {
                            if(s == null) return action.getNamespace();
                            return s;
                        })
                        .build()
                ,
                new FunctionProperties.Builder().build());
    }

    @Override
    public boolean execute(Action action, Object... Args) {
        ActionConfig actionConfig = (ActionConfig) Args[0];
        HashMap<String, Object> placeholders = (HashMap<String, Object>) Args[1];
        String namespace = (String) Args[2];
        ActionCreateEvent event = new ActionCreateEvent(action);
        Action actionExe = action.getActionManager().createAction(actionConfig, placeholders, namespace, event);
        actionExe.load().run();
        return false;
    }
}
