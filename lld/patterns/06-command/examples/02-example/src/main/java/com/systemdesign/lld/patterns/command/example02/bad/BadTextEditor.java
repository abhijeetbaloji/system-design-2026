package com.systemdesign.lld.patterns.command.example02.bad;

import java.util.ArrayList;
import java.util.List;

/**
 * BAD DESIGN:
 * An editor that achieves undo by taking a full copy of the entire document string
 * on every single keystroke.
 *
 * Flaws:
 * 1. Severe Memory Leak & Inefficiency:
 *    For a 10MB document, typing 100 characters stores 100 full copies (1GB of RAM!).
 * 2. No Redo Support:
 *    Reverting an undo is difficult without another parallel stack of complete snapshots.
 * 3. Monolithic Class:
 *    All string manipulation, snapshot copying, and history tracking are tightly
 *    coupled in one class.
 */
public class BadTextEditor {

    private String text = "";
    private final List<String> history = new ArrayList<>();

    public void type(String newText) {
        history.add(text); // Full memory snapshot copy!
        text = text + newText;
        System.out.println("Typed: " + newText + " | Buffer: " + text);
    }

    public void deleteLast(int charCount) {
        history.add(text); // Full snapshot copy
        int cutIndex = Math.max(0, text.length() - charCount);
        text = text.substring(0, cutIndex);
        System.out.println("Deleted " + charCount + " chars | Buffer: " + text);
    }

    public void undo() {
        if (!history.isEmpty()) {
            text = history.remove(history.size() - 1);
            System.out.println("Undo! Restored Buffer: " + text);
        } else {
            System.out.println("Nothing to undo.");
        }
    }

    public String getText() {
        return text;
    }

    public int getHistorySize() {
        return history.size();
    }
}
