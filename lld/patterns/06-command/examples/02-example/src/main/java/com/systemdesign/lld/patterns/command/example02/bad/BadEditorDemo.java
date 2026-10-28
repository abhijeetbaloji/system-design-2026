package com.systemdesign.lld.patterns.command.example02.bad;

/**
 * Runner demonstrating bad design problems.
 */
public class BadEditorDemo {
    public static void main(String[] args) {
        BadTextEditor editor = new BadTextEditor();
        editor.type("Hello ");
        editor.type("World!");
        editor.deleteLast(1); // delete '!'
        editor.undo();
        editor.undo();
        System.out.println("Final text: " + editor.getText());
    }
}
