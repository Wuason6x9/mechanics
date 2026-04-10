package dev.wuason.mechanics;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.wuason.adapter.Adapter;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import dev.wuason.mechanics.utils.AdventureUtils;
import dev.wuason.mechanics.utils.StorageUtils;
import dev.wuason.nms.wrappers.NMSManager;
import dev.wuason.nms.wrappers.VersionWrapper;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;

public class CommandManager {
    private Mechanics core;

    public CommandManager(Mechanics core) {
        this.core = core;
        loadCommand();
    }

    private LiteralCommandNode<CommandSourceStack> mainCommand() {
        return Commands.literal("mechanics")
                .requires(stack -> stack.getSender().hasPermission("mechanics.command.main"))
                .then(Commands.literal("manager")
                        .then(Commands.literal("getAll")
                                .executes(ctx -> {
                                    ctx.getSource().getSender().sendMessage(Arrays.stream(Bukkit.getPluginManager().getPlugins())
                                            .filter(plugin -> plugin instanceof MechanicAddon)
                                            .map(plugin -> plugin.getName())
                                            .toList().toString());
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("debug")
                        .requires(stack -> stack.getSender().hasPermission("mechanics.command.debug"))
                        .then(Commands.literal("sendToastPlayer")
                                .then(Commands.argument("player", ArgumentTypes.player())
                                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                                .executes(ctx -> {
                                                    Player player = ctx.getArgument("player", Player.class);
                                                    String message = StringArgumentType.getString(ctx, "message");
                                                    NMSManager.getVersionWrapper().sendToast(player, new ItemStack(Material.DIAMOND), AdventureUtils.deserializeJson(message, player), VersionWrapper.ToastType.TASK);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("getNBT")
                                .executes(ctx -> {
                                    // Logic was empty in original
                                    return 1;
                                })
                        )
                        .then(Commands.literal("test")
                                .requires(stack -> stack.getSender().hasPermission("mechanics.command.debug.test"))
                                .executes(ctx -> {
                                    // Logic was empty in original
                                    return 1;
                                })
                        )
                        .then(Commands.literal("miniMessageFormatToJson")
                                .then(Commands.argument("format", StringArgumentType.greedyString())
                                        .executes(ctx -> {
                                            String format = StringArgumentType.getString(ctx, "format");
                                            ctx.getSource().getSender().sendMessage("(" + AdventureUtils.deserializeJson(format, null) + ")");
                                            System.out.println(AdventureUtils.deserializeJson(format, null));
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("openInventoryTestMiniMessage")
                                .then(Commands.argument("format", StringArgumentType.greedyString())
                                        .executes(ctx -> {
                                            if (!(ctx.getSource().getSender() instanceof Player player)) return 0;
                                            String format = StringArgumentType.getString(ctx, "format");
                                            System.out.println(AdventureUtils.deserializeLegacy(format, player));
                                            Inventory inventory = Bukkit.createInventory(player, 54, AdventureUtils.deserializeLegacy(format, player));
                                            player.openInventory(inventory);
                                            return 1;
                                        })
                                )
                        )
                )
                .then(Commands.literal("adapter")
                        .then(Commands.literal("get")
                                .then(Commands.argument("id", StringArgumentType.string())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1, 64))
                                                .executes(ctx -> {
                                                    if (!(ctx.getSource().getSender() instanceof Player player)) return 0;
                                                    String id = StringArgumentType.getString(ctx, "id");
                                                    int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                                    return executeGet(player, id, amount);
                                                })
                                        )
                                        .executes(ctx -> {
                                            if (!(ctx.getSource().getSender() instanceof Player player)) return 0;
                                            String id = StringArgumentType.getString(ctx, "id");
                                            return executeGet(player, id, 1);
                                        })
                                )
                        )
                        .then(Commands.literal("give")
                                .then(Commands.argument("players", ArgumentTypes.players())
                                        .then(Commands.argument("id", StringArgumentType.string())
                                                .then(Commands.argument("amount", IntegerArgumentType.integer(1, 64))
                                                        .executes(ctx -> {
                                                            Collection<Player> players = ctx.getArgument("players", Collection.class);
                                                            String id = StringArgumentType.getString(ctx, "id");
                                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                                            return executeGive(players, id, amount);
                                                        })
                                                )
                                                .executes(ctx -> {
                                                    Collection<Player> players = ctx.getArgument("players", Collection.class);
                                                    String id = StringArgumentType.getString(ctx, "id");
                                                    return executeGive(players, id, 1);
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("getAdapterId")
                                .then(Commands.argument("sendToConsole", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            if (!(ctx.getSource().getSender() instanceof Player player)) return 0;
                                            boolean sendToConsole = BoolArgumentType.getBool(ctx, "sendToConsole");
                                            return executeGetAdapterId(player, sendToConsole, false);
                                        })
                                )
                                .executes(ctx -> {
                                    if (!(ctx.getSource().getSender() instanceof Player player)) return 0;
                                    return executeGetAdapterId(player, false, false);
                                })
                        )
                        .then(Commands.literal("getAdapterIdAdvanced")
                                .then(Commands.argument("sendToConsole", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            if (!(ctx.getSource().getSender() instanceof Player player)) return 0;
                                            boolean sendToConsole = BoolArgumentType.getBool(ctx, "sendToConsole");
                                            return executeGetAdapterId(player, sendToConsole, true);
                                        })
                                )
                                .executes(ctx -> {
                                    if (!(ctx.getSource().getSender() instanceof Player player)) return 0;
                                    return executeGetAdapterId(player, false, true);
                                })
                        )
                )
                .build();
    }

    private int executeGet(Player player, String id, int amount) {
        if (!Adapter.exists(id)) return 0;
        ItemStack itemStack = Adapter.getItemStack(id);
        if (itemStack == null) return 0;
        itemStack.setAmount(amount);
        StorageUtils.addItemToInventoryOrDrop(player, itemStack);
        return 1;
    }

    private int executeGive(Collection<Player> players, String id, int amount) {
        if (players.isEmpty() || !Adapter.exists(id)) return 0;
        ItemStack itemStack = Adapter.getItemStack(id);
        if (itemStack == null) return 0;
        itemStack.setAmount(amount);
        for (Player player : players) {
            StorageUtils.addItemToInventoryOrDrop(player, itemStack);
        }
        return 1;
    }

    private int executeGetAdapterId(Player player, boolean sendToConsole, boolean advanced) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType().isAir()) return 0;
        String adapterId = advanced ? Adapter.getAdvancedAdapterId(itemStack) : Adapter.getAdapterId(itemStack);
        AdventureUtils.sendMessage(player, String.format("<gold>id: <aqua> <hover:show_text:'<red>click to copy the clipboard'><click:copy_to_clipboard:%s>%s</click>", adapterId, adapterId));
        if (sendToConsole) {
            AdventureUtils.sendMessagePluginConsole("<gold>id: <aqua>" + adapterId);
        }
        return 1;
    }

    public void loadCommand() {
        core.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(mainCommand(), "mechanics command for testing and others");
        });
    }
}
