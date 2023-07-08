package dev.wuason.mechanics;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.wuason.mechanics.mechanics.Mechanic;
import dev.wuason.mechanics.utils.AdventureUtils;
import org.bukkit.entity.Player;

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
                    core.getMechanicsManager().reloadMechanics();
                })
        );
        command.withSubcommands(new CommandAPICommand("manager")

                .withSubcommands(new CommandAPICommand("start")

                        .withArguments(new StringArgument("mechanic_name_jar"))
                        .executes((sender, args) -> {

                            String mechanicName = (String) args[0];
                            sender.sendMessage(AdventureUtils.deserializeLegacy("<gold>Starting mechanic!"));
                            core.getMechanicsManager().startMechanic(mechanicName);

                        })

                )
                .withSubcommands(new CommandAPICommand("stop")

                        .withArguments(new StringArgument("mechanic_id"))
                        .executes((sender, args) -> {

                            String mechanicName = (String) args[0];
                            sender.sendMessage(AdventureUtils.deserializeLegacy("<gold>Stopping mechanic!"));
                            Mechanic mechanic = core.getMechanicsManager().getMechanic(mechanicName);
                            Boolean started = mechanic != null ? core.getMechanicsManager().stopMechanic(mechanic) : false;
                            if(!started){
                                sender.sendMessage(AdventureUtils.deserializeLegacy("<gold>Error stopping mechanic!"));
                            }

                        })

                )

        );
        command.register();

    }

    public CommandAPICommand getCommand() {
        return command;
    }
}
