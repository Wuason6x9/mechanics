package dev.wuason.mechanics.actions.functions.def.actions;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.functions.Function;
import dev.wuason.mechanics.actions.functions.FunctionArgument;
import dev.wuason.mechanics.actions.functions.FunctionArgumentProperties;
import dev.wuason.mechanics.actions.functions.FunctionProperties;
import dev.wuason.mechanics.actions.utils.ActionConfigUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JavaCodeFunction extends Function {

    public static final FunctionProperties PROPERTIES = new FunctionProperties.Builder().build();



    public JavaCodeFunction() {
        super("JAVACODE", new FunctionArgument.Builder()
                .addArgument(0, "code", (s, action, objects) -> {
                    if(s == null) return "";
                    return s;
                }, builder -> builder.setProcessArgSearchArgs(false))
                .addArgument(1, "imports", (s, action, objects) -> {

                    ArrayList<String> imports = new ArrayList<>();
                    if(s != null){
                        imports.addAll(ActionConfigUtils.getListFromArg(s));
                    }
                    String importLine = ActionConfigUtils.getImportsLine(imports);
                    action.runCode(importLine);
                    return null;
                })
                        .build()
                , PROPERTIES);
    }

    @Override
    public boolean execute(Action action, Object... args) {

        String code = (String) args[0];

        action.runCode(code);

        return false;
    }
}
