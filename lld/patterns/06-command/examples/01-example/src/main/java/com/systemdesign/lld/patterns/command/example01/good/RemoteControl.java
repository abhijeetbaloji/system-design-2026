package com.systemdesign.lld.patterns.command.example01.good;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Invoker Class:
 * Smart Remote Control featuring slots for ON and OFF commands and multi-level Undo.
 *
 * Notice:
 * 1. Has zero references to Light, Stereo, or CeilingFan.
 * 2. Unassigned slots are pre-filled with NoCommand (Null Object Pattern).
 * 3. Tracks executed commands in an undo stack.
 */
public class RemoteControl {

    private final Command[] onCommands;
    private final Command[] offCommands;
    private final Deque<Command> undoStack;

    public RemoteControl(int slotCount) {
        if (slotCount <= 0) {
            throw new IllegalArgumentException("Slot count must be positive");
        }
        this.onCommands = new Command[slotCount];
        this.offCommands = new Command[slotCount];
        this.undoStack = new ArrayDeque<>();

        Command noCommand = new NoCommand();
        for (int i = 0; i < slotCount; i++) {
            onCommands[i] = noCommand;
            offCommands[i] = noCommand;
        }
    }

    public RemoteControl() {
        this(7);
    }

    public void setCommand(int slot, Command onCommand, Command offCommand) {
        validateSlot(slot);
        this.onCommands[slot] = onCommand != null ? onCommand : new NoCommand();
        this.offCommands[slot] = offCommand != null ? offCommand : new NoCommand();
    }

    public void pressOnButton(int slot) {
        validateSlot(slot);
        onCommands[slot].execute();
        undoStack.push(onCommands[slot]);
    }

    public void pressOffButton(int slot) {
        validateSlot(slot);
        offCommands[slot].execute();
        undoStack.push(offCommands[slot]);
    }

    public void pressUndoButton() {
        if (!undoStack.isEmpty()) {
            Command lastCommand = undoStack.pop();
            lastCommand.undo();
        } else {
            System.out.println("Nothing to undo.");
        }
    }

    private void validateSlot(int slot) {
        if (slot < 0 || slot >= onCommands.length) {
            throw new IndexOutOfBoundsException("Invalid remote slot: " + slot + ", max: " + (onCommands.length - 1));
        }
    }

    public int getSlotCount() { return onCommands.length; }
    public boolean canUndo() { return !undoStack.isEmpty(); }
}
