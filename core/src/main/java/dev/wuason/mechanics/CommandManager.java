package dev.wuason.mechanics;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import dev.wuason.mechanics.compatibilities.adapter.Adapter;
import dev.wuason.mechanics.items.ItemBuilderMechanic;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import dev.wuason.mechanics.utils.AdventureUtils;
import dev.wuason.mechanics.utils.StorageUtils;
import dev.wuason.nms.wrappers.ServerNmsVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.IntStream;

public class CommandManager {
    private Mechanics core;
    private CommandAPICommand command;

    public CommandManager(Mechanics core) {
        this.core = core;
        loadCommand();
    }

    public void loadCommand(){

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
                .withSubcommands(new CommandAPICommand("test")
                        .executes((sender, args) -> {
                            Player player = (Player) sender;

                            ServerNmsVersion.getVersionWrapper().openSing(player, (lines) -> {
                                StringBuilder str = new StringBuilder();
                                for(int i=0;i<lines.length;i++){
                                    if(lines[i] == null) return;
                                    str.append(lines[i].toString());
                                }
                                player.sendMessage(">" + str.toString() + "<");
                            });

                        })
                )
                .withSubcommands(new CommandAPICommand("miniMessageFormatToJson")
                        .withArguments(new GreedyStringArgument("format"))
                        .executes((sender, args) -> {
                            String format = (String) args.get(0);
                            sender.sendMessage( "(" + AdventureUtils.deserializeJson(format,null) + ")");
                            System.out.println(AdventureUtils.deserializeJson(format,null));
                        })
                )
                .withSubcommands(new CommandAPICommand("openInventoryTestMiniMessage")
                        .withArguments(new GreedyStringArgument("format"))
                        .executes((sender, args) -> {
                            Player player = (Player) sender;
                            String format = (String) args.get(0);
                            System.out.println(AdventureUtils.deserializeLegacy(format,player));
                            Inventory inventory = Bukkit.createInventory(player,54,AdventureUtils.deserializeLegacy(format,player));
                            player.openInventory(inventory);
                        })
                )
        );
        command.withSubcommands(new CommandAPICommand("adapter")
                .withSubcommands(new CommandAPICommand("get")
                        .withArguments(new TextArgument("id"))
                        .withArguments(new IntegerArgument("amount"))
                        .executes((sender, args) -> {
                            Adapter adapterManager = Adapter.getInstance();
                            String id = (String) args.get(0);
                            if(!adapterManager.existAdapterID(id)) return;
                            ItemStack itemStack = adapterManager.getItemStack(id);
                            int amount = (int) args.get(1);
                            if(amount>64||amount<1) amount = 64;
                            itemStack.setAmount(amount);
                            if(itemStack == null) return;
                            StorageUtils.addItemToInventoryOrDrop((Player) sender,itemStack);
                        })
                )
                .withSubcommands(new CommandAPICommand("give")
                        .withArguments(new EntitySelectorArgument.ManyPlayers("player"))
                        .withArguments(new TextArgument("id"))
                        .withArguments(new IntegerArgument("amount"))
                        .executes((sender, args) -> {
                            Collection<Player> players = (Collection<Player>) args.get(0);
                            if(players.isEmpty()) return;
                            Adapter adapterManager = Adapter.getInstance();
                            String id = (String) args.get(1);
                            if(!adapterManager.existAdapterID(id)) return;
                            ItemStack itemStack = adapterManager.getItemStack(id);
                            int amount = (int) args.get(2);
                            if(amount>64||amount<1) amount = 64;
                            itemStack.setAmount(amount);
                            if(itemStack == null) return;
                            for(Player player : players){
                                StorageUtils.addItemToInventoryOrDrop(player,itemStack);
                            }
                        })
                )
                .withSubcommands(new CommandAPICommand("getAdapterID")
                        .executes((sender, args) -> {
                            Player player = (Player) sender;
                            if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
                            ItemStack itemStack = player.getInventory().getItemInMainHand();
                            Adapter adapterManager = Adapter.getInstance();
                            String adapterId = adapterManager.getAdapterID(itemStack);
                            player.sendMessage(adapterId);
                            AdventureUtils.sendMessagePluginConsole("<gold>id: <aqua>" + adapterId);
                        })
                )
        );
        command.register();

    }
}
