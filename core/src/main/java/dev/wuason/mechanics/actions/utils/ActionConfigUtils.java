package dev.wuason.mechanics.actions.utils;

import dev.wuason.mechanics.actions.args.Arguments;
import dev.wuason.mechanics.actions.config.ArgumentConfig;
import dev.wuason.mechanics.actions.config.ConditionConfig;
import dev.wuason.mechanics.actions.config.FunctionConfig;
import dev.wuason.mechanics.actions.functions.Function;
import dev.wuason.mechanics.actions.functions.Functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class ActionConfigUtils {
    public static ConditionConfig getCondition(String line){
        String replacementPlaceHolder = "$%$";
        String replacement = line;
        int charFirstResult = -1;
        int charLastResult = -1;
        boolean a = false;
        if(!line.contains("{") || !line.contains("}") || line.trim().equals("")) return null;
        HashMap<String, ArgumentConfig> replacements = new HashMap<>();
        while(!a){
            charFirstResult = line.indexOf("{", charFirstResult + 1);
            charLastResult = line.indexOf("}", charLastResult + 1);
            if(charFirstResult == -1 || charLastResult == -1) break;
            String placeHolder = replacementPlaceHolder.replace("%", UUID.randomUUID().toString().replace("-",""));
            ArgumentConfig arg = ArgumentConfigUtils.getArg(line.substring(charFirstResult + 1,charLastResult));
            replacements.put(placeHolder, arg);
            replacement = replacement.replace(line.substring(charFirstResult,charLastResult + 1),placeHolder);
        }
        return new ConditionConfig(replacements, line, replacement);
    }

    public static FunctionConfig getFunction(String line){
        int indexOfFirst = line.indexOf("{");
        int indexOfLast = line.lastIndexOf("}");
        if(indexOfFirst == -1 || indexOfLast == -1) return null;
        String argsLine = line.substring(indexOfFirst + 1, indexOfLast);
        ArrayList<String[]> args = getFunctionArguments(argsLine);
        Function function = Functions.FUNCTIONS.get(line.substring(0, indexOfFirst).replace(" ", "").toUpperCase(Locale.ENGLISH));
        if(function.equals("")) return null;
        FunctionConfig functionConfig = new FunctionConfig(function,args);
        return functionConfig;
    }

    public static ArrayList<String[]> getFunctionArguments(String content){
        String[] s = content.split(" /(?![^\\[\\]]*\\])(?![^{}]*\\})(?![^()]*\\))");
        ArrayList<String[]> args = new ArrayList<>();
        for(String split : s){
            int charResult = split.indexOf("=");
            String keyArg = split.substring(0,charResult).replace(" ", "").toUpperCase(Locale.ENGLISH);
            String valueArg = split.substring(charResult + 1).trim();
            args.add(new String[]{keyArg,valueArg});
        }
        return args;
    }
}
