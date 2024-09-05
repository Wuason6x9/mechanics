package dev.wuason.mechanics.actions.utils;

import dev.wuason.mechanics.actions.config.FunctionInternalConfig;

import java.util.HashMap;
import java.util.Locale;

public class FunctionInternalUtils {
    /**
     * Retrieves the {@link FunctionInternalConfig} for a given line.
     *
     * @param line The line containing the function information.
     * @return The {@link FunctionInternalConfig} object representing the function configuration.
     */
    public static FunctionInternalConfig getFunctionInternalConfig(String line) {
        String id = line.substring(0, line.indexOf("(")).trim().toUpperCase(Locale.ENGLISH);
        String argsLine = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")).trim();
        HashMap<String, String> argsMap = new HashMap<>();
        String[] args = argsLine.split(" /(?![^\\[\\]]*\\])(?![^{}]*\\})(?![^()]*\\))");
        for (int i = 0; i < args.length; i++) {
            String[] arg = ArgumentConfigUtils.getArgRaw(args[i]);
            argsMap.put(arg[0], arg[1]);
        }
        return new FunctionInternalConfig(id, argsMap);
    }

}
