package dev.wuason.mechanics.actions.args.def.internal.functions.def.adapter;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternal;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternalArgument;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternalArgumentProperties;
import dev.wuason.mechanics.actions.args.def.internal.functions.FunctionInternalProperties;
import dev.wuason.mechanics.items.ItemBuilder;
import dev.wuason.mechanics.utils.AdventureUtils;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class GetItemStackByAdapter extends FunctionInternal {

    public static final String ID = "getItemStackByAdapter";

    public static final FunctionInternalProperties PROPERTIES = new FunctionInternalProperties.Builder().build();

    public static final HashMap<String, FunctionInternalArgument> ARGS = new HashMap<String, FunctionInternalArgument>(){{
        FunctionInternalArgument item = new FunctionInternalArgument("item", 1, new FunctionInternalArgumentProperties.Builder().build()) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {
                if(line == null) return null;
                return new ItemBuilder(line, (int)args[0]).build();
            }
        };
        FunctionInternalArgument amount = new FunctionInternalArgument("amount", 0, new FunctionInternalArgumentProperties.Builder().build()) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {
                if(line == null) return 1;
                int amount = 1;
                try {
                    amount = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    return 1;
                }
                if(amount < 1) amount = 1;
                if(amount > 64) amount = 64;
                return amount;
            }
        };
        FunctionInternalArgument name = new FunctionInternalArgument("name", 2, new FunctionInternalArgumentProperties.Builder().build()) {
            @Override
            public Object computeArg(String line, Action action, Object... args) {
                if(line == null) return null;
                return AdventureUtils.deserializeLegacy(line, null);
            }
        };
        put(item.getName(), item);
        put(amount.getName(), amount);
        put(name.getName(), name);
    }};




    public GetItemStackByAdapter() {
        super(PROPERTIES, ARGS, "getItemStackByAdapter");
    }

    @Override
    public Object compute(Action action, Object... args) {
        if(args[2] == null) return args[1];
        return new ItemBuilder((ItemStack) args[1]).setNameWithMiniMessage((String) args[2]).build();
    }
}
