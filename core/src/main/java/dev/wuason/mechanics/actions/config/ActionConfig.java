package dev.wuason.mechanics.actions.config;

import dev.wuason.mechanics.actions.events.EventAction;
import dev.wuason.mechanics.actions.executators.Executor;
import dev.wuason.mechanics.actions.executators.Run;

import java.util.ArrayList;
import java.util.Collection;

public class ActionConfig {

    private final Collection<String> imports;
    private final Run runType;
    private final Executor executor;
    private final String eventAction;
    private final String id;
    private final ArrayList<VarConfig> vars;
    private final ArrayList<VarListConfig<?>> varsList;
    private final ArrayList<FunctionConfig> functions;
    private final ArrayList<ConditionConfig> conditions;

    public ActionConfig(Collection<String> imports, Run runType, Executor executor, String eventAction, String id, ArrayList<VarConfig> vars, ArrayList<VarListConfig<?>> varsList, ArrayList<FunctionConfig> functions, ArrayList<ConditionConfig> conditions) {
        this.imports = imports;
        this.runType = runType;
        this.executor = executor;
        this.eventAction = eventAction;
        this.id = id;
        this.vars = vars;
        this.varsList = varsList;
        this.functions = functions;
        this.conditions = conditions;
    }

    public Collection<String> getImports() {
        return imports;
    }

    public String getImportsLine(){
        if(getImports().size() < 1) return "";
        StringBuilder sb = new StringBuilder();
        for (String s : imports) {
            sb.append("import ").append(s.trim()).append(";");
        }
        return sb.toString();
    }

    public Run getRunType() {
        return runType;
    }

    public Executor getExecutor() {
        return executor;
    }

    public String getEventAction() {
        return eventAction;
    }

    public String getId() {
        return id;
    }

    public ArrayList<VarConfig> getVars() {
        return vars;
    }

    public ArrayList<VarListConfig<?>> getVarsList() {
        return varsList;
    }

    public ArrayList<FunctionConfig> getFunctions() {
        return functions;
    }

    public ArrayList<ConditionConfig> getConditions() {
        return conditions;
    }



}
