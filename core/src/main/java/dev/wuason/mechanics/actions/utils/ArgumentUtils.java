package dev.wuason.mechanics.actions.utils;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.Arguments;
import dev.wuason.mechanics.actions.config.ArgumentConfig;
import dev.wuason.mechanics.utils.MathUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentUtils {

    private static final Pattern PATTERN_PERCENT = Pattern.compile("%(.*?)%");
    private static final Pattern PATTERN_DOLLAR = Pattern.compile("\\$(.*?)\\$");
    private static final Pattern PATTERN_KEY = Pattern.compile("\\{(.*?)\\}");



    public static String processArg(String arg, Action action) {

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

    public static String processArgSearchArgs(String arg, Action action) {
        Set<String> placeholdersFound = findNestedPatterns(arg);
        for (String p : placeholdersFound) {
            if(!ArgumentConfigUtils.isArg(p)) continue;
            ArgumentConfig argConfig = ArgumentConfigUtils.getArg(p);
            String varOriginal = "<" + p + ">";
            String randomGen = UUID.randomUUID().toString().replace("-","");
            String varGen = ("$" + randomGen + "$");
            Argument argument = Arguments.createArgument(argConfig);
            Object objComputed = argument.computeArg(action);
            action.registerPlaceholder(varGen, objComputed);
            arg = arg.replace(varOriginal, varGen);
        }
        return arg;
    }

    public static Set<String> findNestedPatterns(String input) {
        Set<String> results = new HashSet<>();
        findNestedPatterns(input, 0, 0, results);
        return results;
    }
    private static void findNestedPatterns(String input, int start, int level, Set<String> results) {
        int openCount = 0;
        int lastOpenIndex = -1;
        for (int i = start; i < input.length(); i++) {
            if (input.charAt(i) == '<') {
                openCount++;
                if (openCount == 1) {
                    lastOpenIndex = i;
                }
            } else if (input.charAt(i) == '>') {
                openCount--;
                if (openCount == 0) {
                    String substring = input.substring(lastOpenIndex + 1, i);
                    if (!substring.contains("<")) {
                        results.add(substring);
                    } else {
                        findNestedPatterns(substring, 0, level + 1, results);
                        results.add(substring);
                    }
                }
            }
        }
    }

}
