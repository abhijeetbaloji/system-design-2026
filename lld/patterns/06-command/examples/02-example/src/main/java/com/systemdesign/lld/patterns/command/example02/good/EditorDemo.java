package com.systemdesign.lld.patterns.command.example02.good;

public class EditorDemo {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();

        System.out.println("--- 1. Typing Initial Text ---");
        editor.type("Hello World");
        System.out.println("Text: " + editor.getText());

        System.out.println("\n--- 2. Inserting Text ---");
        editor.insert(6, "Brave ");
        System.out.println("Text: " + editor.getText());

        System.out.println("\n--- 3. Replacing Text ---");
        editor.replace(6, 5, "New");
        System.out.println("Text: " + editor.getText());

        System.out.println("\n--- 4. Undo Replace ---");
        editor.undo();
        System.out.println("After 1st Undo: " + editor.getText());

        System.out.println("\n--- 5. Undo Insert ---");
        editor.undo();
        System.out.println("After 2nd Undo: " + editor.getText());

        System.out.println("\n--- 6. Redo Insert ---");
        editor.redo();
        System.out.println("After Redo: " + editor.getText());
    }
}
