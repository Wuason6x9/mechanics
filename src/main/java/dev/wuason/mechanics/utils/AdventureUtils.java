package dev.wuason.mechanics.utils;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.mechanics.Mechanic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class AdventureUtils {

    public static String PREFIX = "<dark_gray>[<gold>$NAME<dark_gray>][<gold>$MECHANIC<dark_gray>] -> <white>";

    public static void consoleMessage(String text){
        MiniMessage mm = MiniMessage.miniMessage();
        Mechanics.getAdventureAudiences().console().sendMessage(mm.deserialize(text));
    }
    public static void playerMessage(String text, Player player){
        MiniMessage mm = MiniMessage.miniMessage();
        Mechanics.getAdventureAudiences().player(player).sendMessage(mm.deserialize(text));
    }

    public static Component deserialize(String text){

        return MiniMessage.miniMessage().deserialize(text);

    }

    public static String deserializeLegacy(String text){

        if(text != null) return LegacyComponentSerializer.legacySection().serialize(AdventureUtils.deserialize(text));

        return null;

    }
    public static List<String> deserializeLegacyList(List<String> listText){

        List<String> deserialized = new ArrayList<>();

        for(String s : listText){

            deserialized.add(LegacyComponentSerializer.legacySection().serialize(AdventureUtils.deserialize(s)));

        }

        return deserialized;

    }

    public static void sendMessagePluginConsole(Plugin plugin, String message){

        consoleMessage(PREFIX.replace("$NAME", Mechanics.getInstance().getDescription().getName()).replace("$MECHANIC",plugin.getDescription().getName()) + message);

    }
    public static void sendMessagePluginConsole(String message){

        consoleMessage(PREFIX.replace("$NAME", Mechanics.getInstance().getDescription().getName()).replace("$MECHANIC","CORE") + message);

    }
}
