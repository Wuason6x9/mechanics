package dev.wuason.mechanics.actions.vars;

public record GlobalVar(String id, Object data) {
    public static boolean isGlobalVar(String line) {
        return line.startsWith("{") && line.endsWith("}");
    }
}
