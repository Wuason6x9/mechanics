package dev.wuason.mechanics.actions.args.def.internal;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.ArgumentProperties;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternal;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternalArgument;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionsInternal;
import dev.wuason.mechanics.actions.config.FunctionInternalConfig;
import dev.wuason.mechanics.actions.utils.ArgumentUtils;
import dev.wuason.mechanics.actions.utils.FunctionInternalUtils;
import dev.wuason.mechanics.utils.AdventureUtils;

public class InternalArg extends Argument {
    public InternalArg(String line, Object[] args) {
        super(line, new ArgumentProperties.Builder().setAutoTransformPlaceholder(true).setReSearchPlaceholders(false).build(), args);
    }

    @Override
    public Object computeArg(Action action, String line) {

        FunctionInternalConfig config = FunctionInternalUtils.getFunctionInternalConfig(line);

        FunctionInternal functionInternal = FunctionsInternal.FUNCTIONS.get(config.getId());
        if(functionInternal == null) {
            AdventureUtils.sendMessagePluginConsole(action.getCore(), "FunctionInternal not found: " + config.getId() + " in line: " + line);
            return null;
        }

        Object[] argsComputed = new Object[functionInternal.getArgumentsRequired().size()];
        FunctionInternalArgument[] orderedArgs = functionInternal.getOrderedArgs();

        for(int i = 0; i < functionInternal.getArgumentsRequired().size(); i++){

            FunctionInternalArgument argF = orderedArgs[i];

            String argContent = config.getArgs().get(argF.getName());

            if(argContent == null && argF.getProperties().isRequired()) {
                AdventureUtils.sendMessagePluginConsole(action.getCore(), "Internal Argument not found: " + argF.getName() + " in function: " + functionInternal.getId() + " in line: " + line);
                return null;
            }

            if(argContent != null){
                if(functionInternal.getProperties().isProcessArgs() && argF.getProperties().isProcessArg()) argContent = ArgumentUtils.processArg(argContent, action);
                if(functionInternal.getProperties().isProcessArgsSearchArgs() && argF.getProperties().isProcessArgSearchArgs()) argContent = ArgumentUtils.processArgSearchArgs(argContent, action);
            }

            argsComputed[i] = argF.computeArgInit(argContent, action, argsComputed);

        }

        return functionInternal.computeInit(action, argsComputed);
    }
}
