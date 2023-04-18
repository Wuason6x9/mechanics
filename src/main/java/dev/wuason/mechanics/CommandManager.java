package dev.wuason.mechanics;

import dev.jorel.commandapi.CommandAPICommand;

public class CommandManager {
    private Mechanics core;
    private CommandAPICommand command;

    public CommandManager(Mechanics core) {
        this.core = core;
        loadCommands();
    }

    public void loadCommands(){

        command = new CommandAPICommand("mechanics");

        command.withPermission("mechanics.command");

    }

    public void registerCommand(){
        command.register();
    }

    public CommandAPICommand getCommand() {
        return command;
    }
}
