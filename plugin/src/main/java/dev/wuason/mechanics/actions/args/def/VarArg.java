package dev.wuason.mechanics.actions.args.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.ArgumentProperties;
import dev.wuason.mechanics.actions.vars.GlobalVar;
import dev.wuason.storagemechanic.storages.Storage;

import java.util.Locale;

public class VarArg extends Argument {
    public VarArg(String line, Object[] args) {
        super(line, new ArgumentProperties.Builder().setAutoGetPlaceholder(false).build(), args);
    }

    @Override
    public Object computeArg(Action action, String line) {
        String var = line.replace(" ", "");
        //Global variable
        if(var.contains("{") && var.contains("}")) {
            String varGlobalVar = getGlobalVarString(var);
            GlobalVar globalVar = action.getActionManager().getGlobalVar(action.getNamespace(), varGlobalVar);
            return globalVar.data();
        }
        return action.getPlaceholder(var);
    }

    private String getGlobalVarString(String string){
        int charFirst = string.indexOf("{");
        int charLast = string.lastIndexOf("}");
        return string.substring(charFirst,charLast + 1).toString();
    }
}
