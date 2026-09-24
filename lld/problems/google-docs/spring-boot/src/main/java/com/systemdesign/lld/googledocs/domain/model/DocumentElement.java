package com.systemdesign.lld.googledocs.domain.model;

import com.systemdesign.lld.googledocs.domain.visitor.DocumentVisitor;

public interface DocumentElement {

    int getLength();

    String getText();

    void accept(DocumentVisitor visitor);
}
