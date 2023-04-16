package dev.wuason.mechanics.utils;

import dev.wuason.mechanics.Mechanics;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdventureUtils {

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
}
