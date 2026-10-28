package com.systemdesign.lld.patterns.command.example01.good;

/**
 * Null Object: Default empty command to avoid null checks in the invoker.
 */
public class NoCommand implements Command {

    @Override
    public void execute() {
        // No-op
    }

    @Override
    public void undo() {
        // No-op
    }
}
