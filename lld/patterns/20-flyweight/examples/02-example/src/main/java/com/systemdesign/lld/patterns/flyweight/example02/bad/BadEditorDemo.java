package com.systemdesign.lld.patterns.flyweight.example02.bad;

import java.util.ArrayList;
import java.util.List;

public class BadEditorDemo {
    public static void main(String[] args) {
        List<BadCharacter> document = new ArrayList<>();
        String sampleText = "The quick brown fox jumps over the lazy dog repeatedly.";

        for (int i = 0; i < sampleText.length(); i++) {
            document.add(new BadCharacter(sampleText.charAt(i), i, "Times New Roman", 12, false, false, "#000000"));
        }

        System.out.println("Allocated " + document.size() + " bad characters with redundant styling strings!");
    }
}
