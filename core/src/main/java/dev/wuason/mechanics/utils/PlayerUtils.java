package dev.wuason.mechanics.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerUtils {
    /**
     * Retrieves a Player object by either their name or UUID.
     *
     * @param nameOrUUID the name or UUID of the player
     * @return the Player object corresponding to the name or UUID, or null if not found
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

    private static UUID stringToUUID(String string) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(string);
        } catch (IllegalArgumentException ignored) {
        }
        return uuid;
    }
}
