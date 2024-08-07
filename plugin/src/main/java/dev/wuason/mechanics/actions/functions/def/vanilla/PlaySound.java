package dev.wuason.mechanics.actions.functions.def.vanilla;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.functions.Function;
import dev.wuason.mechanics.actions.functions.FunctionArgument;
import dev.wuason.mechanics.actions.functions.FunctionProperties;
import dev.wuason.mechanics.utils.PlayerUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PlaySound extends Function {


    public PlaySound() {
        super("playSound",
                new FunctionArgument.Builder()
                        .addArgument(0, "player", (s, action, objects) -> {
                            Objects.requireNonNull(s);
                            Player player = PlayerUtils.getPlayer(s);
                            Objects.requireNonNull(player);
                            return player;
                        }, builder -> {
                            builder.setRequired(true);
                        })
                        .addArgument(1, "sound", (s, action, objects) -> {
                            Objects.requireNonNull(s);
                            return s;
                        }, builder -> {
                            builder.setRequired(true);
                        })
                        .addArgument(2, "volume", (s, action, objects) -> {
                            float volume = 100.0F;
                            if(s == null) return volume / 100.0F;
                            return Float.parseFloat(s) / 100.0F;
                        }, builder -> {
                            builder.setRequired(false);
                        })
                        .addArgument(3, "pitch", (s, action, objects) -> {
                            float pitch = 100.0F;
                            if(s == null) return (pitch / 100.0F) * 0.2F;
                            return (Float.parseFloat(s) / 100.0F) * 0.2F;
                        }, builder -> {
                            builder.setRequired(false);
                        })

                        .build()
                ,
                new FunctionProperties.Builder()
                        .build()
        );
    }

    @Override
    public boolean execute(Action action, Object... Args) {
        Player player = (Player) Args[0];
        String sound = (String) Args[1];
        float volume = (float) Args[2];
        float pitch = (float) Args[3];
        player.playSound(Sound.sound(Key.key(sound), Sound.Source.MASTER, volume, pitch));
        return false;
    }
}
