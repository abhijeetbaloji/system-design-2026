package com.systemdesign.lld.googledocs.good;

public class PlainTextExporter implements DocumentExporter {

    @Override
    public String export(Document document) {
        return document.getText();
    }
}
