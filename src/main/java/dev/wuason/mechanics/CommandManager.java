package dev.wuason.mechanics;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.wuason.mechanics.compatibilities.AdapterManager;
import dev.wuason.mechanics.items.ItemBuilderMechanic;
import dev.wuason.mechanics.mechanics.Mechanic;
import dev.wuason.mechanics.utils.AdventureUtils;
import dev.wuason.mechanics.utils.StorageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

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
        command.withSubcommands(new CommandAPICommand("reload")
                .executes((sender, args) -> {
                    sender.sendMessage("reloading mechanics!");
                    core.getManager().getMechanicsManager().reloadMechanics();
                })
        );
        command.withSubcommands(new CommandAPICommand("manager")

                .withSubcommands(new CommandAPICommand("start")

                        .withArguments(new StringArgument("mechanic_name_jar"))
                        .executes((sender, args) -> {

                            String mechanicName = (String) args.get(0);
                            sender.sendMessage(AdventureUtils.deserializeLegacy("<gold>Starting mechanic!"));
                            core.getManager().getMechanicsManager().startMechanic(mechanicName);

                        })

                )
                .withSubcommands(new CommandAPICommand("stop")

                        .withArguments(new StringArgument("mechanic_id"))
                        .executes((sender, args) -> {
                            String mechanicName = (String) args.get(0);;
                            sender.sendMessage(AdventureUtils.deserializeLegacy("<gold>Stopping mechanic!"));
                            Mechanic mechanic = core.getManager().getMechanicsManager().getMechanic(mechanicName);
                            Boolean started = mechanic != null ? core.getManager().getMechanicsManager().stopMechanic(mechanic) : false;
                            if(!started){
                                sender.sendMessage(AdventureUtils.deserializeLegacy("<gold>Error stopping mechanic!"));
                            }

                        })

                )

        );
        command.withSubcommands(new CommandAPICommand("adapter")
                .withSubcommands(new CommandAPICommand("get")
                        .withArguments(new StringArgument("id"))
                        .withArguments(new IntegerArgument("amount"))
                        .executes((sender, args) -> {
                            AdapterManager adapterManager = core.getManager().getAdapterManager();
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
                        .withArguments(new StringArgument("id"))
                        .withArguments(new IntegerArgument("amount"))
                        .executes((sender, args) -> {
                            Collection<Player> players = (Collection<Player>) args.get(0);
                            if(players.isEmpty()) return;
                            AdapterManager adapterManager = core.getManager().getAdapterManager();
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
                            AdapterManager adapterManager = core.getManager().getAdapterManager();
                            player.sendMessage(adapterManager.getAdapterID(itemStack));
                        })
                )
        );

        command.register();

    }

    public CommandAPICommand getCommand() {
        return command;
    }
}
