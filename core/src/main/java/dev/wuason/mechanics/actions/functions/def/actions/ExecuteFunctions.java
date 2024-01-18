package dev.wuason.mechanics.actions.functions.def.actions;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.config.ConditionConfig;
import dev.wuason.mechanics.actions.config.FunctionConfig;
import dev.wuason.mechanics.actions.executators.Run;
import dev.wuason.mechanics.actions.functions.Function;
import dev.wuason.mechanics.actions.functions.FunctionArgument;
import dev.wuason.mechanics.actions.functions.FunctionProperties;
import dev.wuason.mechanics.actions.utils.ActionConfigUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExecuteFunctions extends Function {
    public ExecuteFunctions() {
        super("executeFunctions",
                new FunctionArgument.Builder().addArgument(0, "functions", (s, action, objects) -> {
                    if(s == null) return new ArrayList<>();
                    return ActionConfigUtils.getListFromArg(s);
                }, builder -> {
                    builder.setAutoGetPlaceholder(true);
                })
                        .addArgument(1, "conditions", (s, action, objects) -> {
                            if(s == null) return new ArrayList<>();
                            return ActionConfigUtils.getListFromArg(s);
                        }, builder -> {
                            builder.setAutoGetPlaceholder(true);
                        })
                        .addArgument(2, "runType", (s, action, objects) -> {
                            if(s == null) return Run.CURRENT;
                            return Run.valueOf(s.toUpperCase());

                        }, builder -> {
                            builder.setAutoGetPlaceholder(false);
                        })
                        .build(),
                new FunctionProperties.Builder().setProcessArgs(false).setProcessArgsSearchArgs(false).build()
        );
    }

    @Override
    public boolean execute(Action action, Object... Args) {

        List<String> functionsLines = (List<String>) Args[0];
        List<String> conditionsLines = (List<String>) Args[1];
        Run runType = (Run) Args[2];

        List<FunctionConfig> functions = new ArrayList<>();
        List<ConditionConfig> conditions = new ArrayList<>();

        for(String line : functionsLines){
            FunctionConfig function = ActionConfigUtils.getFunction(line);
            if(function != null) functions.add(function);
        }

        for(String line : conditionsLines){
            ConditionConfig condition = ActionConfigUtils.getCondition(line);
            if(condition != null) {
                conditions.add(condition);
                action.loadCondition(condition);
            }
        }

        for(FunctionConfig function : functions){
            action.execute(function,runType,conditions);
        }

        return false;
    }

}

