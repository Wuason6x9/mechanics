package dev.wuason.mechanics.actions.utils;

import java.util.Locale;

public class ArgumentConfigUtils {
    public static String[] getArg(String content){
        String[] arg = new String[2];
        int charResult = content.indexOf("=");
        arg[0] = content.substring(0,charResult).replace(" ","").toUpperCase(Locale.ENGLISH);
        arg[1] = content.substring(charResult + 1).trim();
        return arg;
    }

    public static boolean isArg(String content){
        return content.contains("=") && getArg(content).length == 2;
    }
}
