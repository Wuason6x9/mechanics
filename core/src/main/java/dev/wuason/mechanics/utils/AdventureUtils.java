package dev.wuason.mechanics.utils;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
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

    /**
     * Sends a message to the console.
     *
     * @param text The text of the message to send. Must not be null.
     */
    public static void consoleMessage(String text){
        if(text == null) return;
        MiniMessage mm = MiniMessage.miniMessage();
        Mechanics.getAdventureAudiences().console().sendMessage(mm.deserialize(text));
    }
    /**
     * Sends a message to a player.
     *
     * @param text   the text to be sent
     * @param player the player to whom the message is sent
     */
    public static void playerMessage(String text, Player player){
        if(text == null || player == null) return;
        MiniMessage mm = MiniMessage.miniMessage();
        if(player != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) text = PlaceholderAPI.setPlaceholders(player, text);
        Mechanics.getAdventureAudiences().player(player).sendMessage(mm.deserialize(text));
    }
    /**
     * Deserializes a JSON string into a serialized Adventure component.
     *
     * @param text   The JSON string to deserialize.
     * @param player The player associated with the text. Can be null.
     * @return The deserialized Adventure component as a string. Returns null if the input text is null.
     */
    public static String deserializeJson(String text, Player player){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) text = PlaceholderAPI.setPlaceholders(player, text);
        if(text != null) return GsonComponentSerializer.gson().serialize(AdventureUtils.deserialize(text));
        return null;
    }
    public static String deserializeJson(String text){
        return deserializeJson(text,null);
    }
    /**
     * Sends a message to the specified CommandSender.
     *
     * @param sender the CommandSender to send the message to
     * @param text   the text of the message
     */
    public static void sendMessage(CommandSender sender, String text){
        if(text == null || sender == null) return;
        MiniMessage mm = MiniMessage.miniMessage();
        if(sender instanceof Player player) {
            if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) text = PlaceholderAPI.setPlaceholders(player, text);
            Mechanics.getAdventureAudiences().player((Player) sender).sendMessage(mm.deserialize(text));
        }
        if(sender instanceof ConsoleCommandSender) Mechanics.getAdventureAudiences().console().sendMessage(mm.deserialize(text));
    }

    /**
     * Deserialize the given text using MiniMessage.
     *
     * @param text the text to be deserialized
     * @return the deserialized Component object
     */
    public static Component deserialize(String text){
        return MiniMessage.miniMessage().deserialize(text);
    }
    /**
     * Deserializes a legacy text string into a modern Adventure Component.
     *
     * @param text The legacy text string to deserialize
     * @param player The player associated with the deserialization process
     * @return The deserialized Adventure Component, or null if the input text is null or cannot be deserialized
     */
    public static String deserializeLegacy(String text, Player player){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) text = PlaceholderAPI.setPlaceholders(player, text);
        if(text != null) return LegacyComponentSerializer.builder().hexColors().build().serialize(AdventureUtils.deserialize(text));
        return null;
    }
    public static String deserializeLegacy(String text){
        return deserializeLegacy(text,null);
    }

    /**
     * Deserialize a list of legacy formatted strings into a list of serialized adventure components.
     *
     * @param listText The list of legacy formatted strings to deserialize.
     * @param player The player whose placeholders need to be resolved. Can be null if no placeholders are used.
     * @return The list of serialized adventure components after deserialization.
     */
    public static List<String> deserializeLegacyList(List<String> listText, Player player){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) listText = PlaceholderAPI.setPlaceholders(player, listText);
        List<String> deserialized = new ArrayList<>();

        for(String s : listText){

            deserialized.add(LegacyComponentSerializer.builder().hexColors().build().serialize(AdventureUtils.deserialize(s)));

        }

        return deserialized;

    }
    public static List<String> deserializeLegacyList(List<String> listText){
        return deserializeLegacyList(listText,null);
    }

    /**
     * Sends a message to the plugin console.
     *
     * @param addon the mechanic addon
     * @param message the message to send
     */
    public static void sendMessagePluginConsole(MechanicAddon addon, String message){

        consoleMessage(PREFIX.replace("$NAME", Mechanics.getInstance().getDescription().getName()).replace("$MECHANIC",((Plugin)addon).getDescription().getName()) + message);

    }
    /**
     * Sends a message to the plugin console.
     *
     * @param message the message to send
     */
    public static void sendMessagePluginConsole(String message){

        consoleMessage(PREFIX.replace("$NAME", Mechanics.getInstance().getDescription().getName()).replace("$MECHANIC","CORE") + message);

    }
}
