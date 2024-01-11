package dev.wuason.mechanics.actions.utils;

import dev.wuason.mechanics.actions.args.Arguments;
import dev.wuason.mechanics.actions.config.ArgumentConfig;

import java.util.Locale;

public class ArgumentConfigUtils {
    public static ArgumentConfig getArg(String content){
        String[] arg = new String[2];
        int charResult = content.indexOf("=");
        arg[0] = content.substring(0,charResult).replace(" ","").toUpperCase(Locale.ENGLISH);
        arg[1] = content.substring(charResult + 1).trim();
        return new ArgumentConfig(Arguments.ARGUMENTS.get(arg[0]),arg[1]);
    }

    public static boolean isArg(String content){
        return content.contains("=") && content.split("=").length == 2;
    }

    public static String[] getArgRaw(String content){
        String[] arg = new String[2];
        int charResult = content.indexOf("=");
        arg[0] = content.substring(0,charResult).replace(" ","").toUpperCase(Locale.ENGLISH);
        arg[1] = content.substring(charResult + 1).trim();
        return arg;
    }
}
