package com.systemdesign.lld.patterns.flyweight.example02.good;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Flyweight Factory that pools and dispenses shared CharacterFormat instances.
 */
public class CharacterFormatFactory {
    private final Map<String, CharacterFormat> formatPool = new ConcurrentHashMap<>();

    public CharacterFormat getFormat(String fontName, int fontSize, boolean bold, boolean italic, String color) {
        Objects.requireNonNull(fontName, "Font name cannot be null");
        Objects.requireNonNull(color, "Color cannot be null");

        String key = String.format("%s_%d_%s_%s_%s", fontName.toLowerCase(), fontSize, bold, italic, color.toLowerCase());
        return formatPool.computeIfAbsent(key, k -> new CharacterFormat(fontName, fontSize, bold, italic, color));
    }

    public int getFormatCount() {
        return formatPool.size();
    }
}
