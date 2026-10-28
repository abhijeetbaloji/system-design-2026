package com.systemdesign.lld.patterns.flyweight.example02.good;

public class TextEditorDemo {
    public static void main(String[] args) {
        TextDocument document = new TextDocument();
        TextEditorService editor = new TextEditorService(document);

        editor.addHeading1("Chapter 1: Flyweight Pattern\n");
        editor.addBodyParagraph("The Flyweight pattern reduces memory by sharing intrinsic state across characters.\n");
        editor.addCodeSnippet("public class FlyweightDemo {}\n");

        System.out.println(editor.printSummary());
        System.out.println("\nPlain Text:\n" + document.toPlainText());

        // Sample render of first 20 characters
        StringBuilder sample = new StringBuilder();
        for (int i = 0; i < Math.min(20, document.getTotalCharacterCount()); i++) {
            document.getCharacters().get(i).draw(sample);
        }
        System.out.println("\nFormatted Sample Stream:\n" + sample);
    }
}
