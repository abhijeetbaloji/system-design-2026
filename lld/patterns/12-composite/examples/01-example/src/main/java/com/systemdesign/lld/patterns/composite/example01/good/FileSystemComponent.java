package com.systemdesign.lld.patterns.composite.example01.good;

import java.util.List;

/**
 * Component interface declaring uniform operations for both files and directories.
 */
public interface FileSystemComponent {
    String getName();

    long getSize();

    String display(String indent);

    List<FileSystemComponent> search(String keyword);

    default boolean isComposite() {
        return false;
    }
}
