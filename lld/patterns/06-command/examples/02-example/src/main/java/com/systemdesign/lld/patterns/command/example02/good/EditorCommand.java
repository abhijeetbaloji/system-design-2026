package com.systemdesign.lld.patterns.command.example02.good;

/**
 * Command Interface for Text Editor operations.
 */
public interface EditorCommand {

    void execute();

    void undo();

    String getActionName();
}
