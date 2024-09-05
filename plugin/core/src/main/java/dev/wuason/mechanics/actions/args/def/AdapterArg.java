package dev.wuason.mechanics.actions.args.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.ArgumentProperties;
import dev.wuason.mechanics.compatibilities.adapter.Adapter;
import org.bukkit.inventory.ItemStack;

import static javax.sound.sampled.AudioSystem.getLine;

public class AdapterArg extends Argument {
    public AdapterArg(String line, Object[] args) {
        super(line, new ArgumentProperties.Builder().setAutoTransformPlaceholder(true).build(),args);
    }
    @Override
    public Object computeArg(Action action, String line) {
        if (Adapter.exists(line)) throw new IllegalArgumentException("Adapter not found: " + line);
        return line;
    }
}
