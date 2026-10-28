package com.systemdesign.lld.patterns.command.example01.good;

/**
 * Command Interface:
 * Declares the execution and reversible undo contracts for all commands.
 */
public interface Command {

    void execute();

    void undo();
}
