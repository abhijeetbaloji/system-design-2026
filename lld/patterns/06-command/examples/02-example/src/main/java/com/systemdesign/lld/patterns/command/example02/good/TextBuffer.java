package com.systemdesign.lld.patterns.command.example02.good;

/**
 * Receiver:
 * In-memory character buffer for document text.
 *
 * Notice:
 * 1. Has zero knowledge of Command, Undo, or Redo.
 * 2. Focuses solely on efficient string buffer mutations.
 */
public class TextBuffer {

    private final StringBuilder buffer = new StringBuilder();

    public void insert(int offset, String text) {
        if (offset < 0 || offset > buffer.length()) {
            throw new IndexOutOfBoundsException("Offset out of bounds: " + offset + ", length: " + buffer.length());
        }
        if (text != null) {
            buffer.insert(offset, text);
        }
    }

    public String delete(int offset, int length) {
        if (offset < 0 || offset + length > buffer.length() || length < 0) {
            throw new IndexOutOfBoundsException("Invalid delete range: offset=" + offset + ", length=" + length);
        }
        String removed = buffer.substring(offset, offset + length);
        buffer.delete(offset, offset + length);
        return removed;
    }

    public String getText() {
        return buffer.toString();
    }

    public int length() {
        return buffer.length();
    }

    public void clear() {
        buffer.setLength(0);
    }
}
