package dev.wuason.mechanics.actions.utils;

import dev.wuason.mechanics.actions.args.Arguments;
import dev.wuason.mechanics.actions.config.ArgumentConfig;

import java.util.Locale;

public class ArgumentConfigUtils {
    /**
     * Retrieves an {@link ArgumentConfig} object based on the given line.
     *
     * @param content The line containing the argument information.
     * @return The {@link ArgumentConfig} object representing the argument.
     */
    public static ArgumentConfig getArg(String content){
        String[] arg = new String[2];
        int charResult = content.indexOf("=");
        arg[0] = content.substring(0,charResult).replace(" ","").toUpperCase(Locale.ENGLISH);
        arg[1] = content.substring(charResult + 1).trim();
        return new ArgumentConfig(Arguments.ARGUMENTS.get(arg[0]),arg[1]);
    }

    /**
     * This method checks if the given content is a valid argument.
     * An argument is considered valid if it contains an equal sign ('=') and has exactly two parts separated by the equal sign.
     *
     * @param content the content to check if it is a valid argument
     * @return true if the content is a valid argument, false otherwise
     */
    public static boolean isArg(String content){
        return content.contains("=") && content.split("=").length == 2;
    }

    /**
     * Parses a string and extracts the raw argument from it.
     *
     * @param content the content string to parse
     * @return an array of strings containing the raw argument
     */
    public static String[] getArgRaw(String content){
        String[] arg = new String[2];
        int charResult = content.indexOf("=");
        arg[0] = content.substring(0,charResult).replace(" ","").toUpperCase(Locale.ENGLISH);
        arg[1] = content.substring(charResult + 1).trim();
        return arg;
    }
}
