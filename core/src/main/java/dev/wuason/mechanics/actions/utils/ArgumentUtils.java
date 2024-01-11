package dev.wuason.mechanics.actions.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentUtils {

    private static final Pattern PATTERN_PERCENT = Pattern.compile("%(.*?)%");
    private static final Pattern PATTERN_DOLLAR = Pattern.compile("\\$(.*?)\\$");
    private static final Pattern PATTERN_KEY = Pattern.compile("\\{(.*?)\\}");

    public static String processArg(String arg, Action action) throws EvalError {

        //%%
        Matcher matcher = PATTERN_PERCENT.matcher(arg);
        List<String> placeholdersFound = new ArrayList<>();
        while (matcher.find()) {
            String pl = matcher.group(1);
            if(!placeholdersFound.contains(pl)) placeholdersFound.add(pl);
        }
        for(String p : placeholdersFound){
            String a = "%" + p + "%";
            if(action.getPlaceholders().containsKey(a.toUpperCase(Locale.ENGLISH).trim().intern())){
                Debug.debug(a.toUpperCase(Locale.ENGLISH).trim().intern());
                arg = arg.replace(a,action.getPlaceholders().get(a.toUpperCase(Locale.ENGLISH).trim().intern()).toString());
            }
        }

    }

}
