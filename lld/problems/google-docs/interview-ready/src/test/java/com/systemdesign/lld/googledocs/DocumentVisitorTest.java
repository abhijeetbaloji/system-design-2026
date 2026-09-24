package com.systemdesign.lld.googledocs;

import com.systemdesign.lld.googledocs.model.Document;
import com.systemdesign.lld.googledocs.model.TextStyle;
import com.systemdesign.lld.googledocs.visitor.HtmlVisitor;
import com.systemdesign.lld.googledocs.visitor.MarkdownVisitor;
import com.systemdesign.lld.googledocs.visitor.PlainTextVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentVisitorTest {

    private Document document;

    @BeforeEach
    void setUp() {
        document = new Document("doc-visitor", "Visitor Architecture", "alice");
    }

    @Test
    void testPlainTextVisitor() {
        document.insert(0, "Plain Text Content", TextStyle.DEFAULT);
        PlainTextVisitor visitor = new PlainTextVisitor();
        document.accept(visitor);

        assertEquals("Plain Text Content", visitor.getOutput());
    }

    @Test
    void testMarkdownVisitor() {
        document.insert(0, "Bold", TextStyle.DEFAULT.withBold(true));
        document.insert(4, " and ", TextStyle.DEFAULT);
        document.insert(9, "Italic", TextStyle.DEFAULT.withItalic(true));

        MarkdownVisitor visitor = new MarkdownVisitor();
        document.accept(visitor);

        String output = visitor.getOutput();
        assertTrue(output.startsWith("# Visitor Architecture\n\n"));
        assertTrue(output.contains("**Bold** and *Italic*"));
    }

    @Test
    void testHtmlVisitor() {
        document.insert(0, "Styled", TextStyle.DEFAULT.withBold(true).withItalic(true));

        HtmlVisitor visitor = new HtmlVisitor();
        document.accept(visitor);

        String html = visitor.getOutput();
        assertTrue(html.contains("<title>Visitor Architecture</title>"));
        assertTrue(html.contains("<b><i>Styled</i></b>"));
    }
}
