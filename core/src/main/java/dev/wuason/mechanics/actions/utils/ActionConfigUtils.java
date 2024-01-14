package dev.wuason.mechanics.actions.utils;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.Arguments;
import dev.wuason.mechanics.actions.config.*;
import dev.wuason.mechanics.actions.functions.Function;
import dev.wuason.mechanics.actions.functions.Functions;

import java.util.*;

public class ActionConfigUtils {
    /**
     * Retrieves a {@link ConditionConfig} object based on the given line.
     *
     * @param line The line containing the condition information.
     * @return The {@link ConditionConfig} object representing the condition, or null if the line is invalid or empty.
     */
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
            String placeHolder = replacementPlaceHolder.replace("%", UUID.randomUUID().toString().replace("-","").toUpperCase(Locale.ENGLISH));
            ArgumentConfig arg = ArgumentConfigUtils.getArg(line.substring(charFirstResult + 1,charLastResult));
            replacements.put(placeHolder, arg);
            replacement = replacement.replace(line.substring(charFirstResult,charLastResult + 1), placeHolder);
        }
        return new ConditionConfig(replacements, line, replacement);
    }

    /**
     * Retrieves a FunctionConfig object based on the given input line.
     *
     * @param line the input line representing a function call
     * @return the FunctionConfig object, or null if the line is not formatted correctly
     */
    public static FunctionConfig getFunction(String line){
        int indexOfFirst = line.indexOf("{");
        int indexOfLast = line.lastIndexOf("}");
        if(indexOfFirst == -1 || indexOfLast == -1) return null;
        String argsLine = line.substring(indexOfFirst + 1, indexOfLast);
        Map<String, String> args = getFunctionArguments(argsLine);
        Function function = Functions.FUNCTIONS.get(line.substring(0, indexOfFirst).replace(" ", "").toUpperCase(Locale.ENGLISH));
        if(function == null) return null;
        FunctionConfig functionConfig = new FunctionConfig(function,args);
        return functionConfig;
    }

    /**
     * Splits the content string into function arguments and returns them as a map.
     *
     * @param content the content string containing the function arguments
     * @return a map of function arguments, where the keys are argument names and the values are argument values
     */
    public static Map<String, String> getFunctionArguments(String content){
        String[] s = content.split(" /(?![^\\[\\]]*\\])(?![^{}]*\\})(?![^()]*\\))");
        Map<String, String> args = new HashMap<>();
        for(String split : s){
            int charResult = split.indexOf("=");
            String keyArg = split.substring(0,charResult).replace(" ", "").toUpperCase(Locale.ENGLISH);
            String valueArg = split.substring(charResult + 1).trim();
            args.put(keyArg, valueArg);
        }
        return Collections.unmodifiableMap(args);
    }

    /**
     * Returns a single line string of import statements based on a given list of import strings.
     *
     * @param imports the list of import strings
     * @return a single line string of import statements
     */
    public static String getImportsLine(List<String> imports){
        String line = "";
        for(String i : imports){
            line = line + "import " + i.trim() + ";";
        }
        return line;
    }

    /**
     * Converts the content of an argument into a list of strings.
     *
     * @param argContent the content of the argument in the format "[value1, value2, value3]"
     * @return an unmodifiable list of strings containing the values extracted from the argument content
     */
    public static List<String> getListFromArg(String argContent) {
        ArrayList<String> list = new ArrayList<>();
        String input = argContent.replace("[", "").replace("]", "").trim();
        String[] segments = input.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?![^\\{]*\\})");
        for(String segment : segments) {
            if (!segment.trim().equals("")) {
                list.add(segment.trim());
            }
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Returns a list of objects extracted from the given argument content using the provided action.
     *
     * @param argContent the argument content to process
     * @param action the action used to process the argument content
     * @return a list of objects extracted from the argument content
     */
    public static List<Object> getListFromArgProcess(String argContent, Action action) {
        ArrayList<Object> list = new ArrayList<>();
        String input = argContent.replace("[", "").replace("]", "").trim();
        String[] segments = input.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?![^\\{]*\\})");
        for(String segment : segments) {
            if (!segment.trim().equals("")) {
                String arg = ArgumentUtils.processArgSearchArgs(ArgumentUtils.processArg(segment.trim(), action), action);
                if(action.hasPlaceholder(arg)) list.add(action.getPlaceholder(arg));
                else list.add(arg);
            }
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Retrieves a VarConfig object from a string representation.
     *
     * @param str the string representation of the VarConfig
     * @return the VarConfig object, or null if the string is invalid or empty
     */
    public static VarConfig getVar(String str){
        int indexOf = str.indexOf("|");
        if(indexOf == -1) return null;
        String varContent = str.substring(indexOf + 1);
        String var = str.substring(0,indexOf).replace(" ", "").toUpperCase(Locale.ENGLISH);
        if(var == "" || varContent == "") return null;
        int resultChar = varContent.indexOf("=");
        String argType = varContent.substring(0, resultChar).replace(" ", "").toUpperCase(Locale.ENGLISH);
        ArgumentConfig argConfig = new ArgumentConfig(Arguments.ARGUMENTS.get(argType), varContent.substring(resultChar + 1 ).trim());
        VarConfig varConfig = new VarConfig(var, argConfig);
        return varConfig;
    }

    /**
     * Retrieves a VarListConfig object with the given parameters.
     *
     * @param var The variable name for the VarListConfig.
     * @param type The type of Argument for the VarListConfig.
     * @param listArgs The list of argument strings for VarListConfig.
     * @return The VarListConfig object with the specified parameters.
     */
    public static VarListConfig<?> getVarList(String var, Class<? extends Argument> type, List<String> listArgs){
        List<ArgumentConfig> arguments = new ArrayList<>();
        for(String arg : listArgs){
            arguments.add(new ArgumentConfig(type, arg.trim()));
        }
        return new VarListConfig(var.replace(" ", "").toUpperCase(Locale.ENGLISH), type, arguments);
    }
}
