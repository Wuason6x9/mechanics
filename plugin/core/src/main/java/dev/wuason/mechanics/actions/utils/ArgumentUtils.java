package dev.wuason.mechanics.actions.utils;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.ArgumentProperties;
import dev.wuason.mechanics.actions.args.Arguments;
import dev.wuason.mechanics.actions.config.ArgumentConfig;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentUtils {

    private static final Pattern PATTERN_PERCENT = Pattern.compile("%(.*?)%");
    private static final Pattern PATTERN_DOLLAR = Pattern.compile("\\$(.*?)\\$");
    private static final Pattern PATTERN_KEY = Pattern.compile("\\{(.*?)\\}");



    /**
     * Processes the given argument based on the provided action.
     *
     * @param arg    The argument to be processed. Must not be null.
     * @param action The action to be performed on the argument.
     * @return The processed argument.
     */
    public static String processArg(String arg, Action action) {
        if(arg == null) return null;
        //************** % % ****************
        Matcher matcherPercent = PATTERN_PERCENT.matcher(arg);
        Set<String> placeholdersFoundPercent = new HashSet<>();
        while (matcherPercent.find()) {
            String pl = matcherPercent.group(1);
            placeholdersFoundPercent.add(pl);
        }
        for(String p : placeholdersFoundPercent){
            String var = "%" + p + "%";
            if(action.hasPlaceholderReplacement(var)){
                arg = arg.replace(var, action.getPlaceholderReplacement(var).toString());
            }
        }
        //************** { } ****************
        Matcher matcherKey = PATTERN_KEY.matcher(arg);
        Set<String> placeholdersFoundKey = new HashSet<>();
        while (matcherKey.find()) {
            String pl = matcherKey.group(1);
            placeholdersFoundKey.add(pl);
        }
        for(String p : placeholdersFoundKey){
            String var = "{" + p + "}";
            if(action.hasPlaceholder(var)){
                arg = arg.replace(var, var.toUpperCase(Locale.ENGLISH));
            }
        }

        //************** $ $ ****************
        Matcher matcherDollar = PATTERN_DOLLAR.matcher(arg);
        Set<String> placeholdersFoundDollar = new HashSet<>();
        while (matcherDollar.find()) {
            String pl = matcherDollar.group(1);
            placeholdersFoundDollar.add(pl);
        }
        for(String p : placeholdersFoundDollar){
            String var = "$" + p + "$";
            if(action.hasPlaceholder(var)){
                arg = arg.replace(var, var.toUpperCase(Locale.ENGLISH));
            }
        }


        return arg;
    }

    /**
     * Processes the argument string by replacing placeholders with computed values.
     *
     * @param arg the argument string with placeholders
     * @param action the Action object that provides the context for argument processing
     * @return the argument string with placeholders replaced by computed values
     */
    public static String processArgSearchArgs(String arg, Action action){

        List<String> placeholders = findPlaceholdersArguments(arg);

        for(int i = 0; i < placeholders.size(); i++){
            String placeholder = placeholders.get(i);
            if(!placeholder.contains("=")) continue;
            String[] argRaw = ArgumentConfigUtils.getArgRaw(placeholder.substring(1, placeholder.length() - 1).trim());

            ArgumentProperties properties = Arguments.getArgumentProperties(argRaw[0]);

            String argProcessed = argRaw[1];

            if(properties.isReSearchPlaceholders()){
                argProcessed = processArgSearchArgs(argProcessed, action);
            }

            String rndPlaceholder = "$" + UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ENGLISH) + "$";

            ArgumentConfig argConfig = ArgumentConfigUtils.getArg(argRaw[0] + "=" + argProcessed);

            Argument argument = Arguments.createArgument(argConfig);

            action.registerPlaceholder(rndPlaceholder, argument.computeArgInit(action));

            arg = arg.replace(placeholder, rndPlaceholder);
        }
        return arg;
    }



    /**
     * Finds the placeholders arguments in a given line.
     *
     * @param line the line to search for placeholders arguments
     * @return the list of placeholders arguments found in the line
     */
    public static List<String> findPlaceholdersArguments(String line) {
        List<String> placeholders = new ArrayList<>();
        int depth = 0;
        int start = -1;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '<') {
                depth++;
                if (depth == 1) {
                    start = i;
                }
            } else if (ch == '>') {
                depth--;
                if (depth == 0 && start != -1) {
                    placeholders.add(line.substring(start, i + 1));
                    start = -1;
                }
            }
        }

        return placeholders;
    }

    /**
     * Retrieves the placeholder argument from the given text using the provided action.
     * A placeholder argument is identified as a non-null and trimmed string that is a valid placeholder in the action.
     *
     * @param text   the text to search for a placeholder argument
     * @param action the action to check for the placeholder
     * @return the placeholder argument if found, otherwise null
     */
    public static Object getArgumentPlaceHolder(String text, Action action){
        return ( text == null || !action.hasPlaceholder(text.trim()) ) ? null : action.getPlaceholder(text.trim());
    }

}
