package dev.wuason.mechanics.commands;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.wuason.mechanics.nms.wrappers.VersionWrapper;

import java.util.Arrays;

public class CustomArguments {
    public static Argument<VersionWrapper.ToastType> toastTypeArgument(String nodeName){

        return new CustomArgument<VersionWrapper.ToastType, String>(new StringArgument(nodeName), info -> {

            String input = info.input();
            VersionWrapper.ToastType toastType = null;
            try {
                toastType = VersionWrapper.ToastType.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }

            if(toastType == null) {
                throw  CustomArgument.CustomArgumentException.fromMessageBuilder(new CustomArgument.MessageBuilder("ToastType not found!"));
            }

            return toastType;

        })
                .replaceSuggestions(ArgumentSuggestions.strings(suggestionInfo -> Arrays.stream(VersionWrapper.ToastType.values()).map(toastType -> toastType.name().toLowerCase()).toArray(String[]::new)));
    }
}
