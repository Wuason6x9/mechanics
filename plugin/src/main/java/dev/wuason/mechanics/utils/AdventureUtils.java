package dev.wuason.mechanics.utils;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentIteratorType;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
        Bukkit.getConsoleSender().sendMessage(mm.deserialize(text));
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
        player.sendMessage(mm.deserialize(text));
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
            player.sendMessage(mm.deserialize(text));
        }
        if(sender instanceof ConsoleCommandSender) Bukkit.getConsoleSender().sendMessage(mm.deserialize(text));
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

    /**
     * This method removes all occurrences of a specified text (placeholder) from a given Component.
     * It does this by converting the Component into a list of TextComponentReplace objects, each representing a single character in the Component.
     * It then iterates over this list, checking each character against the characters in the placeholder.
     * If a sequence of characters matching the placeholder is found, these characters are marked for removal.
     * After all occurrences of the placeholder have been marked, they are removed from the Component.
     * Finally, the modified Component is reconstructed and returned.
     *
     * @param component The Component from which to remove the placeholder. Must not be null.
     * @param placeholder The text to remove from the Component. Must not be null.
     * @return A new Component with all occurrences of the placeholder removed.
     */

    public static Component removeTextAllComponents(Component component, String placeholder) {
        List<TextComponentReplace> replaces = convertToTextComponentReplace(component);
        List<TextComponentReplace> toReplace = new ArrayList<>();
        List<TextComponentReplace> replaced = new ArrayList<>();
        for (TextComponentReplace replace : replaces) {
            if (replace.getChar() == placeholder.charAt(toReplace.size())) {
                toReplace.add(replace);
                if (toReplace.size() == placeholder.length()) {
                    replaced.addAll(toReplace);
                    toReplace.clear();
                }
            }
            else if (!toReplace.isEmpty()) {
                toReplace.clear();
            }
        }
        for (int i = replaced.size() - 1; i >= 0; i--) {
            replaced.get(i).removeCharByIndex();
        }
        Iterator<Component> componentIterator = component.iterator(ComponentIteratorType.DEPTH_FIRST);
        Component cNew = Component.empty();
        while (componentIterator.hasNext()) {
            Component c = componentIterator.next();
            for (TextComponentReplace replace : replaced) {
                if(replace.getComponent().getUnmodifiedParent() == c) {
                    c = replace.getComponent().getParent();
                }
            }
            cNew = cNew.append(c.children(new ArrayList<>()));
        }
        return cNew;
    }


    public static List<TextComponentReplace> convertToTextComponentReplace(Component component) {
        List<TextComponentReplace> replaces = new ArrayList<>();
        component.iterator(ComponentIteratorType.DEPTH_FIRST).forEachRemaining((c) -> {
            if (c instanceof TextComponent txtC) {
                ComponentParent parent = new ComponentParent(txtC);
                char[] chars = txtC.content().toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    replaces.add(new TextComponentReplace(parent, chars[i], i));
                }
            }
        });
        return replaces;
    }

    public static String getAllTextFromComponent(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }


    public static boolean containsText(Component component, String text) {

        return getAllTextFromComponent(component).contains(text);
    }

    public static boolean containsTextIgnoreCase(Component component, String text) {
        return getAllTextFromComponent(component).toUpperCase(Locale.ENGLISH).contains(text.toUpperCase(Locale.ENGLISH));
    }

    public static class ComponentParent {
        private TextComponent parent;
        private final TextComponent unmodifiedParent;

        public ComponentParent(TextComponent parent) {
            this.parent = parent;
            this.unmodifiedParent = parent;
        }

        public TextComponent getParent() {
            return parent;
        }

        public void setParent(TextComponent parent) {
            this.parent = parent;
        }

        public TextComponent getUnmodifiedParent() {
            return unmodifiedParent;
        }
    }

    public static class TextComponentReplace {
        private final ComponentParent component;
        private final char character;
        private final int characterIndex;

        public TextComponentReplace(ComponentParent component, char character, int characterIndex) {
            this.component = component;
            this.character = character;
            this.characterIndex = characterIndex;
        }

        public void removeCharByIndex() {
            StringBuilder builder = new StringBuilder();
            builder.append(component.getParent().content());
            builder.deleteCharAt(characterIndex);
            component.setParent(component.getParent().content(builder.toString()));
        }

        public char getChar() {
            return character;
        }

        public int getCharIndex() {
            return characterIndex;
        }

        public ComponentParent getComponent() {
            return component;
        }
    }



}
