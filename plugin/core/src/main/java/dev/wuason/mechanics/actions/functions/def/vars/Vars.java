package dev.wuason.mechanics.actions.functions.def.vars;

import dev.wuason.mechanics.actions.functions.Functions;

public class Vars {

    public static void register() {

        Functions.registerFunction(builder -> {
            builder.setName("refreshLocalVar");
            builder.addArguments(argBuilder -> {
                argBuilder.addArgument(0, "var", (s, action, objects) -> {
                    if(objects[0] == null) return "";
                    return s;
                }, pBuilder -> {
                    pBuilder.setRequired(true);
                    pBuilder.setAutoGetPlaceholder(false);
                    pBuilder.setProcessArgSearchArgs(false);
                });
            });
            builder.setProperties(propertiesBuilder -> {
                propertiesBuilder.setProcessArgsSearchArgs(false);
            });
            builder.setExecute((action, args) -> {

                String var = (String) args[0];

                if(!action.hasPlaceholder(var)) return false;

                action.refreshPlaceholder(var);

                return false;
            });
        });

        Functions.registerFunction(builder -> {
            builder.setName("refreshAllLocalVars");
            builder.setExecute((action, args) -> {
                action.refreshAllPlaceholders();
                return false;
            });
        });

        Functions.registerFunction( fBuilder -> {

            fBuilder.setName("setValueLocalVar");

            fBuilder.addArguments( argBuilder ->{

                argBuilder.addArgument(0, "var", (s, action, objects) -> {
                    if(objects[0] == null) return "";
                    return s;
                }, pBuilder -> {
                    pBuilder.setRequired(true);
                    pBuilder.setAutoGetPlaceholder(false);
                    pBuilder.setProcessArgSearchArgs(false);
                });

                argBuilder.addArgument(1, "value", (s, action, objects) -> s, pBuilder -> {
                    pBuilder.setRequired(true);
                    pBuilder.setAutoGetPlaceholder(true);
                    pBuilder.setProcessArgSearchArgs(true);
                });

            });

            fBuilder.setExecute((action, args) -> {
                String var = (String) args[0];
                Object value = args[1];
                action.registerPlaceholder(var, value);
                return false;

            });

        });

        //********** GLOBAL VARS FUNCTION **********//

        Functions.registerFunction( fBuilder -> {

            fBuilder.setName("setValueGlobalVar");

            fBuilder.addArguments( argBuilder ->{

                argBuilder.addArgument(0, "var", (s, action, objects) -> {
                    if(objects[0] == null) return "";
                    return s;
                }, pBuilder -> {
                    pBuilder.setRequired(true);
                    pBuilder.setAutoGetPlaceholder(false);
                    pBuilder.setProcessArgSearchArgs(false);
                });

                argBuilder.addArgument(1, "value", (s, action, objects) -> s, pBuilder -> {
                    pBuilder.setRequired(true);
                    pBuilder.setAutoGetPlaceholder(true);
                    pBuilder.setProcessArgSearchArgs(true);
                });

                argBuilder.addArgument(2, "namespace", (s, action, objects) -> {
                    if(s == null) return action.getNamespace();
                    return s;
                }, pBuilder -> {
                    pBuilder.setRequired(false);
                    pBuilder.setAutoGetPlaceholder(true);
                    pBuilder.setProcessArgSearchArgs(true);
                });

            });

            fBuilder.setExecute((action, args) -> {
                String var = (String) args[0];
                Object value = args[1];
                String namespace = (String) args[2];
                action.getActionManager().setValueGlobalVar(namespace, var, value);
                return false;
            });

        });

        //removeGlobalVar

        Functions.registerFunction( fBuilder -> {

            fBuilder.setName("removeGlobalVar");

            fBuilder.addArguments( argBuilder ->{

                argBuilder.addArgument(0, "var", (s, action, objects) -> {
                    if(objects[0] == null) return "";
                    return s;
                }, pBuilder -> {
                    pBuilder.setRequired(true);
                    pBuilder.setAutoGetPlaceholder(false);
                    pBuilder.setProcessArgSearchArgs(false);
                });

                argBuilder.addArgument(1, "namespace", (s, action, objects) -> {
                    if(s == null) return action.getNamespace();
                    return s;
                }, pBuilder -> {
                    pBuilder.setRequired(false);
                    pBuilder.setAutoGetPlaceholder(true);
                    pBuilder.setProcessArgSearchArgs(true);
                });

            });

            fBuilder.setExecute((action, args) -> {
                String var = (String) args[0];
                String namespace = (String) args[1];
                action.getActionManager().removeGlobalVar(namespace, var);
                return false;
            });

        });

    }
}
