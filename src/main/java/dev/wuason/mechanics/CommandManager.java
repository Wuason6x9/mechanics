package dev.wuason.mechanics;

import dev.jorel.commandapi.CommandAPICommand;

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
                    core.getMechanicsManager().loadMechanics();
                })
        );
        command.register();

    }

    public CommandAPICommand getCommand() {
        return command;
    }
}
