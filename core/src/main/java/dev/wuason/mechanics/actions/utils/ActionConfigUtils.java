package dev.wuason.mechanics.actions.utils;

import dev.wuason.mechanics.actions.config.ConditionConfig;

import java.util.HashMap;
import java.util.UUID;

public class ActionConfigUtils {
    public static ConditionConfig getCondition(String line){
        String replacementPlaceHolder = "$%$";
        String replacement = line;
        int charFirstResult = -1;
        int charLastResult = -1;
        boolean a = false;
        if(!line.contains("{") || !line.contains("}") || line.trim().equals("")) return null;
        HashMap<String, String[]> replacements = new HashMap<>();
        while(!a){
            charFirstResult = line.indexOf("{", charFirstResult + 1);
            charLastResult = line.indexOf("}", charLastResult + 1);
            if(charFirstResult == -1 || charLastResult == -1) break;
            String placeHolder = replacementPlaceHolder.replace("%", UUID.randomUUID().toString().replace("-",""));
            replacements.put(placeHolder, ArgumentConfigUtils.getArg(line.substring(charFirstResult + 1,charLastResult)));
            replacement = replacement.replace(line.substring(charFirstResult,charLastResult + 1),placeHolder);
        }
        return new ConditionConfig(replacements,line,replacement);
    }
}
