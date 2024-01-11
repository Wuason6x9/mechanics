package dev.wuason.mechanics.actions.config;

import java.util.HashMap;

public class ConditionConfig {
    private HashMap<String, ArgumentConfig[]> replacements;
    private String line;
    private String replacement;

    public ConditionConfig(HashMap<String, ArgumentConfig[]> replacements, String line, String replacement) {
        this.replacements = replacements;
        this.line = line;
        this.replacement = replacement;
    }

    public HashMap<String, ArgumentConfig[]> getReplacements() {
        return replacements;
    }

    public String getLine() {
        return line;
    }

    public String getReplacement() {
        return replacement;
    }
}