package dev.wuason.mechanics.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerUtils {
    /**
     * Retrieves a Player object based on a given name or UUID.
     *
     * @param nameOrUUID the name or UUID of the player
     * @return the Player object if found, otherwise null
     */
    public static Player getPlayer(@NotNull String nameOrUUID) {
        Player player = null;
        UUID uuid = stringToUUID(nameOrUUID);
        if (uuid != null) {
            player = Bukkit.getPlayer(uuid);
        } else {
            player = Bukkit.getPlayer(nameOrUUID);
        }
        return player;
    }

    /**
     * Converts a string representation of a UUID to an actual UUID object.
     * If the input string is not a valid UUID, it returns null.
     *
     * @param string the string to be converted to a UUID
     * @return the UUID object, or null if the string is not a valid UUID
     */
    private static UUID stringToUUID(String string) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(string);
        } catch (IllegalArgumentException ignored) {
        }
        return uuid;
    }
}
