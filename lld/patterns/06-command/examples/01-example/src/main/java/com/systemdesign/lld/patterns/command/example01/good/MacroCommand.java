package com.systemdesign.lld.patterns.command.example01.good;

import java.util.List;

/**
 * Composite Command:
 * Combines multiple commands into a single batch execution.
 * Executes in forward order, and undos in reverse order!
 */
public class MacroCommand implements Command {

    private final List<Command> commands;

    public MacroCommand(List<Command> commands) {
        this.commands = commands != null ? List.copyOf(commands) : List.of();
    }

    @Override
    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }

    @Override
    public void undo() {
        // Reverse order undo for natural rollback
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }
}
