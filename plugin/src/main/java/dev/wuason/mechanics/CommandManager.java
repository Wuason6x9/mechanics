package dev.wuason.mechanics;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.wuason.adapter.Adapter;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import dev.wuason.mechanics.utils.AdventureUtils;
import dev.wuason.mechanics.utils.StorageUtils;
import dev.wuason.nms.wrappers.NMSManager;
import dev.wuason.nms.wrappers.VersionWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;

public class CommandManager {
    private Mechanics core;
    private CommandAPICommand command;

    public CommandManager(Mechanics core) {
        this.core = core;
        loadCommand();
    }

    public void loadCommand() {

        command = new CommandAPICommand("mechanics");
        command.withPermission("mechanics.command.main");
        command.withSubcommands(new CommandAPICommand("manager")

                .withSubcommands(new CommandAPICommand("getAll")
                        .executes((sender, args) -> {
                            sender.sendMessage(Arrays.stream(Bukkit.getPluginManager().getPlugins()).filter(plugin -> plugin instanceof MechanicAddon).map(plugin -> plugin.getName()).toList().toString());
                        })
                )
        );
        command.withSubcommands(new CommandAPICommand("debug")
                .withPermission("mechanics.command.debug")
                .withSubcommands(new CommandAPICommand("sendToastPlayer")
                        .withArguments(new EntitySelectorArgument.OnePlayer("player"))
                        .withArguments(new GreedyStringArgument("message"))
                        .executes((sender, args) -> {
                            Player player = (Player) args.get(0);
                            String message = (String) args.get(1);

                            NMSManager.getVersionWrapper().sendToast(player, new ItemStack(Material.DIAMOND), AdventureUtils.deserializeJson(message, player), VersionWrapper.ToastType.TASK);
                        })
                )
                .withSubcommands(new CommandAPICommand("getNBT")
                        .executesPlayer((player, commandArguments) -> {


                        })
                )
                .withSubcommands(new CommandAPICommand("test")
                        .withPermission("mechanics.command.debug.test")
                        .executes((sender, args) -> {

                        })
                )
                .withSubcommands(new CommandAPICommand("miniMessageFormatToJson")
                        .withArguments(new GreedyStringArgument("format"))
                        .executes((sender, args) -> {
                            String format = (String) args.get(0);
                            sender.sendMessage("(" + AdventureUtils.deserializeJson(format, null) + ")");
                            System.out.println(AdventureUtils.deserializeJson(format, null));
                        })
                )
                .withSubcommands(new CommandAPICommand("openInventoryTestMiniMessage")
                        .withArguments(new GreedyStringArgument("format"))
                        .executes((sender, args) -> {
                            Player player = (Player) sender;
                            String format = (String) args.get(0);
                            System.out.println(AdventureUtils.deserializeLegacy(format, player));
                            Inventory inventory = Bukkit.createInventory(player, 54, AdventureUtils.deserializeLegacy(format, player));
                            player.openInventory(inventory);
                        })
                )
        );
        command.withSubcommands(new CommandAPICommand("adapter")
                .withSubcommands(new CommandAPICommand("get")
                        .withArguments(new TextArgument("id"))
                        .withArguments(new IntegerArgument("amount").setOptional(true))
                        .executes((sender, args) -> {
                            String id = (String) args.get(0);
                            if (!Adapter.exists(id)) return;
                            ItemStack itemStack = Adapter.getItemStack(id);
                            int amount = (int) args.getOrDefault(1, 1);
                            if (amount > 64 || amount < 1) amount = 64;
                            itemStack.setAmount(amount);
                            if (itemStack == null) return;
                            StorageUtils.addItemToInventoryOrDrop((Player) sender, itemStack);
                        })
                )
                .withSubcommands(new CommandAPICommand("give")
                        .withArguments(new EntitySelectorArgument.ManyPlayers("player"))
                        .withArguments(new TextArgument("id"))
                        .withArguments(new IntegerArgument("amount").setOptional(true))
                        .executes((sender, args) -> {
                            Collection<Player> players = (Collection<Player>) args.get(0);
                            if (players.isEmpty()) return;
                            String id = (String) args.get(1);
                            if (!Adapter.exists(id)) return;
                            ItemStack itemStack = Adapter.getItemStack(id);
                            int amount = (int) args.getOrDefault(2, 1);
                            if (amount > 64 || amount < 1) amount = 64;
                            itemStack.setAmount(amount);
                            if (itemStack == null) return;
                            for (Player player : players) {
                                StorageUtils.addItemToInventoryOrDrop(player, itemStack);
                            }
                        })
                )
                .withSubcommands(new CommandAPICommand("getAdapterId")
                        .withArguments(new BooleanArgument("sendToConsole").setOptional(true))
                        .executes((sender, args) -> {
                            Player player = (Player) sender;
                            if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR))
                                return;
                            ItemStack itemStack = player.getInventory().getItemInMainHand();
                            String adapterId = Adapter.getAdapterId(itemStack);

                            AdventureUtils.sendMessage(player, String.format("<gold>id: <aqua> <hover:show_text:'<red>click to copy the clipboard'><click:copy_to_clipboard:%s>%s</click>", adapterId, adapterId));
                            boolean sendToConsole = (boolean) args.getOrDefault(0, false);
                            if (sendToConsole) {
                                AdventureUtils.sendMessagePluginConsole("<gold>id: <aqua>" + adapterId);
                            }
                        })
                )
                .withSubcommands(new CommandAPICommand("getAdapterIdAdvanced")
                        .withArguments(new BooleanArgument("sendToConsole").setOptional(true))
                        .executes((sender, args) -> {
                            Player player = (Player) sender;
                            if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR))
                                return;
                            ItemStack itemStack = player.getInventory().getItemInMainHand();
                            String adapterId = Adapter.getAdvancedAdapterId(itemStack);
                            AdventureUtils.sendMessage(player, String.format("<gold>id: <aqua> <hover:show_text:'<red>click to copy the clipboard'><click:copy_to_clipboard:%s>%s</click>", adapterId, adapterId));
                            boolean sendToConsole = (boolean) args.getOrDefault(0, false);
                            if (sendToConsole) {
                                AdventureUtils.sendMessagePluginConsole("<gold>id: <aqua>" + adapterId);
                            }
                        })
                )
        );
        command.register();

    }
}
