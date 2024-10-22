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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class AdventureUtils {

    /**
     * A string used as a prefix for messages, which includes color codes for formatting.
     * The prefix is intended to be used in message formatting, replacing the `$NAME` placeholder with the desired value.
     */
    public static String PREFIX = "<dark_gray>[<gold>$NAME<dark_gray>] -> <white>";

    /**
     * Converts legacy color code text into a modern format by replacing legacy color codes with corresponding tags.
     *
     * @param text the legacy color code text to be converted
     * @return the text with legacy color codes replaced by modern tags
     */
    public static String fromLegacyText(String text) {
        return text
                .replace("&0", "<black>")
                .replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>")
                .replace("&5", "<dark_purple>")
                .replace("&6", "<gold>")
                .replace("&7", "<gray>")
                .replace("&8", "<dark_gray>")
                .replace("&9", "<blue>")
                .replace("&a", "<green>")
                .replace("&b", "<aqua>")
                .replace("&c", "<red>")
                .replace("&d", "<light_purple>")
                .replace("&e", "<yellow>")
                .replace("&f", "<white>")
                .replace("&k", "<obfuscated>")
                .replace("&l", "<bold>")
                .replace("&m", "<strikethrough>")
                .replace("&n", "<underline>")
                .replace("&o", "<italic>")
                .replace("&r", "<reset>");
    }


    /**
     * Sends a deserialized text message to the console.
     *
     * @param text The message to be sent to the console. If the text is null, the method returns immediately.
     */
    public static void consoleMessage(String text) {
        if (text == null) return;
        MiniMessage mm = MiniMessage.miniMessage();
        Bukkit.getConsoleSender().sendMessage(mm.deserialize(text));
    }

    /**
     * Sends a message to the specified player. The message can contain placeholders
     * if the PlaceholderAPI plugin is available.
     *
     * @param text   The message text to be sent to the player.
     * @param player The player who will receive the message.
     */
    public static void playerMessage(String text, Player player) {
        if (text == null || player == null) return;
        MiniMessage mm = MiniMessage.miniMessage();
        if (player != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            text = PlaceholderAPI.setPlaceholders(player, text);
        player.sendMessage(mm.deserialize(text));
    }

    /**
     * Deserializes a JSON text string and processes PlaceholderAPI placeholders if applicable.
     *
     * @param text the JSON text to deserialize
     * @param player the player context for PlaceholderAPI (can be null)
     * @return the serialized string after deserialization, or null if the input text is null
     */
    public static String deserializeJson(String text, Player player) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            text = PlaceholderAPI.setPlaceholders(player, text);
        if (text != null) return GsonComponentSerializer.gson().serialize(AdventureUtils.deserialize(text));
        return null;
    }

    /**
     * Deserializes a given JSON string into its corresponding representation, potentially processing
     * placeholders if PlaceholderAPI is present.
     *
     * @param text the JSON text to be deserialized
     * @return the deserialized representation of the JSON text, or null if the text is null
     */
    public static String deserializeJson(String text) {
        return deserializeJson(text, null);
    }

    /**
     * Sends a formatted message to the specified command sender. The method supports both
     * players and console command senders. If the PlaceholderAPI plugin is available and the
     * sender is a player, placeholders in the text will be replaced accordingly.
     *
     * @param sender the recipient of the message. This can be either a Player or a ConsoleCommandSender.
     * @param text the message to be sent. If the text is null, the method will return immediately.
     */
    public static void sendMessage(CommandSender sender, String text) {
        if (text == null || sender == null) return;
        MiniMessage mm = MiniMessage.miniMessage();
        if (sender instanceof Player player) {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                text = PlaceholderAPI.setPlaceholders(player, text);
            player.sendMessage(mm.deserialize(text));
        }
        if (sender instanceof ConsoleCommandSender) Bukkit.getConsoleSender().sendMessage(mm.deserialize(text));
    }

    /**
     * Deserializes a given string into a Component object using the MiniMessage format.
     *
     * @param text the string representation of the Component in MiniMessage format
     * @return the deserialized Component object
     */
    public static Component deserialize(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }

    /**
     * Deserializes a list of strings into a list of Component objects.
     *
     * @param list The list of strings to be deserialized.
     * @return A list of deserialized Component objects.
     */
    public static List<Component> deserialize(List<String> list) {
        List<Component> components = new ArrayList<>();
        for (String s : list) {
            components.add(deserialize(s));
        }
        return components;
    }

    /**
     * Deserializes the given legacy text for a specific player, converting legacy color codes
     * into components and applying any applicable PlaceholderAPI placeholders.
     *
     * @param text The legacy text to deserialize.
     * @param player The player for whom the placeholders should be applied.
     * @return The deserialized text as a string, or null if the input text is null.
     */
    public static String deserializeLegacy(String text, Player player) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            text = PlaceholderAPI.setPlaceholders(player, text);
        if (text != null)
            return LegacyComponentSerializer.builder().hexColors().build().serialize(AdventureUtils.deserialize(text));
        return null;
    }

    /**
     * Deserializes a legacy formatted text string using the specified text.
     *
     * @param text The legacy formatted text to be deserialized.
     * @return The deserialized text using legacy formatting.
     */
    public static String deserializeLegacy(String text) {
        return deserializeLegacy(text, null);
    }

    /**
     * Deserializes a list of legacy-formatted text strings and applies PlaceholderAPI placeholders if available.
     *
     * @param listText a list of strings containing legacy-formatted text.
     * @param player the player for whom the placeholders should be applied, can be null if PlaceholderAPI is not needed.
     * @return a new list of strings with serialized components from the deserialized legacy text.
     */
    public static List<String> deserializeLegacyList(List<String> listText, Player player) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            listText = PlaceholderAPI.setPlaceholders(player, listText);
        List<String> deserialized = new ArrayList<>();

        for (String s : listText) {

            deserialized.add(LegacyComponentSerializer.builder().hexColors().build().serialize(AdventureUtils.deserialize(s)));

        }

        return deserialized;

    }

    /**
     * Deserializes a list of legacy text strings into a list of modern formatted text strings, applying placeholders if applicable.
     *
     * @param listText the list of legacy text strings to be deserialized
     * @return a list of deserialized text strings
     */
    public static List<String> deserializeLegacyList(List<String> listText) {
        return deserializeLegacyList(listText, null);
    }

    /**
     * Sends a message to the console that includes the name of the specified addon.
     *
     * @param addon  the MechanicAddon instance whose name will be included in the message
     * @param message the message to be sent to the console
     */
    public static void sendMessagePluginConsole(MechanicAddon addon, String message) {

        consoleMessage(PREFIX.replace("$NAME", addon.getName()) + message);

    }

    /**
     * Sends a message to the plugin console with a prefixed format.
     *
     * @param message the message to be sent to the plugin console.
     */
    public static void sendMessagePluginConsole(String message) {

        consoleMessage(PREFIX.replace("$NAME", Mechanics.getInstance().getName()) + message);

    }

    /**
     * Serializes a Component into its legacy text form.
     *
     * @param component the Component to serialize
     * @return the serialized legacy text representation of the Component
     */

    public static String serialize(Component component) {
        return LegacyComponentSerializer.builder().hexColors().build().serialize(component);
    }

    /**
     * Removes occurrences of a specified placeholder text from the provided component and its nested components.
     *
     * @param component the root component from which placeholder text will be removed
     * @param placeholder the text sequence to be removed from all components
     * @return a new component with the specified placeholder text removed
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
            } else if (!toReplace.isEmpty()) {
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
                if (replace.getComponent().getUnmodifiedParent() == c) {
                    c = replace.getComponent().getParent();
                }
            }
            cNew = cNew.append(c.children(new ArrayList<>()));
        }
        return cNew;
    }


    /**
     * Converts a given component into a list of {@code TextComponentReplace}
     * objects, where each object represents a character in the text components
     * of the original component.
     *
     * @param component The component from which to create the list of
     * {@code TextComponentReplace} objects.
     * @return A list of {@code TextComponentReplace} objects representing
     * individual characters in the text components of the given component.
     */
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

    /**
     * Retrieves all plain text contained within a given Component.
     *
     * @param component the component from which the text is to be extracted
     * @return a string containing all plain text from the specified component
     */
    public static String getAllTextFromComponent(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }


    /**
     * Checks if the textual content of a given Component contains a specific substring.
     *
     * @param component the Component whose text content is to be checked
     * @param text the substring to look for within the component's text content
     * @return true if the component's text contains the specified substring, false otherwise
     */
    public static boolean containsText(Component component, String text) {

        return getAllTextFromComponent(component).contains(text);
    }

    /**
     * Checks if the provided text is present within the given component's text, ignoring case differences.
     *
     * @param component the Component object from which to extract text
     * @param text the String text to search for within the component's text
     * @return true if the text is found within the component's text, ignoring case differences; false otherwise
     */
    public static boolean containsTextIgnoreCase(Component component, String text) {
        return getAllTextFromComponent(component).toUpperCase(Locale.ENGLISH).contains(text.toUpperCase(Locale.ENGLISH));
    }

    /**
     * The ComponentParent class manages a parent TextComponent and its unmodified state.
     */
    public static class ComponentParent {
        /**
         * The parent TextComponent within the ComponentParent class.
         * It can be modified after initialization.
         */
        private TextComponent parent;
        /**
         * The original TextComponent instance representing the unmodified state of the parent component.
         * This variable is initialized once and is intended to remain immutable to serve as a reference
         * to the component's initial state.
         */
        private final TextComponent unmodifiedParent;

        /**
         * Constructs a {@code ComponentParent} object that holds a reference to the specified parent {@code TextComponent}.
         *
         * @param parent The {@code TextComponent} object that acts as the parent of this {@code ComponentParent}.
         */
        public ComponentParent(TextComponent parent) {
            this.parent = parent;
            this.unmodifiedParent = parent;
        }

        /**
         * Retrieves the parent TextComponent of this component.
         *
         * @return the parent TextComponent
         */
        public TextComponent getParent() {
            return parent;
        }

        /**
         * Sets the parent TextComponent for this component.
         *
         * @param parent the TextComponent instance to set as parent
         */
        public void setParent(TextComponent parent) {
            this.parent = parent;
        }

        /**
         * Retrieves the unmodified parent TextComponent of the current instance.
         *
         * @return the unmodified parent TextComponent associated with this instance
         */
        public TextComponent getUnmodifiedParent() {
            return unmodifiedParent;
        }
    }

    /**
     * TextComponentReplace is a utility class that deals with character
     * replacement within a parent text component.
     */
    public static class TextComponentReplace {
        /**
         * The `ComponentParent` associated with the `TextComponentReplace` instance.
         * This variable holds the parent `TextComponent` and its unmodified state.
         */
        private final ComponentParent component;
        /**
         * The character to be replaced or modified within the TextComponent.
         */
        private final char character;
        /**
         * Represents the index position of a character within the parent component's content.
         */
        private final int characterIndex;

        /**
         * Constructs a TextComponentReplace object that associates a character, its
         * index in the text, and the parent component containing it.
         *
         * @param component the parent component which contains the text
         * @param character the character to be replaced
         * @param characterIndex the index of the character in the text
         */
        public TextComponentReplace(ComponentParent component, char character, int characterIndex) {
            this.component = component;
            this.character = character;
            this.characterIndex = characterIndex;
        }

        /**
         * Removes a character at the specified index from the content of this component's parent.
         *
         * This method modifies the content of the parent component by deleting the character
         * located at the given characterIndex. The updated content is then set back to the parent component.
         *
         * Note: Ensure that the characterIndex is within the valid range before calling this method
         * to avoid IndexOutOfBoundsException.
         */
        public void removeCharByIndex() {
            StringBuilder builder = new StringBuilder();
            builder.append(component.getParent().content());
            builder.deleteCharAt(characterIndex);
            component.setParent(component.getParent().content(builder.toString()));
        }

        /**
         * Gets the character associated with this TextComponentReplace instance.
         *
         * @return the character associated with this instance
         */
        public char getChar() {
            return character;
        }

        /**
         * Retrieves the index of the character within the text component.
         *
         * @return the index of the character
         */
        public int getCharIndex() {
            return characterIndex;
        }

        /**
         * Retrieves the ComponentParent instance associated with this TextComponentReplace.
         *
         * @return the ComponentParent instance associated with this TextComponentReplace
         */
        public ComponentParent getComponent() {
            return component;
        }
    }


}
