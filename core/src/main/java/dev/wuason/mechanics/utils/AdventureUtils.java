package dev.wuason.mechanics.utils;

import dev.wuason.mechanics.Mechanics;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class AdventureUtils {

    public static String PREFIX = "<dark_gray>[<gold>$NAME<dark_gray>][<gold>$MECHANIC<dark_gray>] -> <white>";

    public static void consoleMessage(String text){
        if(text == null) return;
        MiniMessage mm = MiniMessage.miniMessage();
        Mechanics.getAdventureAudiences().console().sendMessage(mm.deserialize(text));
    }
    public static void playerMessage(String text, Player player){
        if(text == null) return;
        MiniMessage mm = MiniMessage.miniMessage();
        if(player != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) text = PlaceholderAPI.setPlaceholders(player, text);
        Mechanics.getAdventureAudiences().player(player).sendMessage(mm.deserialize(text));
    }
    public static void sendMessage(CommandSender sender, String text){
        if(text == null || sender == null) return;
        MiniMessage mm = MiniMessage.miniMessage();
        if(sender instanceof Player) {
            if((Player)sender != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) text = PlaceholderAPI.setPlaceholders((Player) sender, text);
            Mechanics.getAdventureAudiences().player((Player) sender).sendMessage(mm.deserialize(text));
        }
        if(sender instanceof ConsoleCommandSender) Mechanics.getAdventureAudiences().console().sendMessage(mm.deserialize(text));
    }

    public static Component deserialize(String text){
        return MiniMessage.miniMessage().deserialize(text);
    }
    public static String deserializeLegacy(String text, Player player){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) text = PlaceholderAPI.setPlaceholders(player, text);
        if(text != null) return LegacyComponentSerializer.legacySection().serialize(AdventureUtils.deserialize(text));

        return null;

    }
    public static List<String> deserializeLegacyList(List<String> listText, Player player){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) listText = PlaceholderAPI.setPlaceholders(player, listText);
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
