package com.systemdesign.lld.patterns.flyweight.example02;

import com.systemdesign.lld.patterns.flyweight.example02.good.CharacterFormat;
import com.systemdesign.lld.patterns.flyweight.example02.good.CharacterFormatFactory;
import com.systemdesign.lld.patterns.flyweight.example02.good.FormattedCharacter;
import com.systemdesign.lld.patterns.flyweight.example02.good.TextDocument;
import com.systemdesign.lld.patterns.flyweight.example02.good.TextEditorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Character Format Flyweight Pattern Tests")
class CharacterFormatFlyweightTest {

    private CharacterFormatFactory factory;
    private TextDocument document;

    @BeforeEach
    void setUp() {
        factory = new CharacterFormatFactory();
        document = new TextDocument(factory);
    }

    @Test
    @DisplayName("Characters sharing identical styling reference the exact same CharacterFormat instance")
    void testFlyweightFormatSharing() {
        document.appendString("Hello World", "Arial", 12, false, false, "#000000");

        assertEquals(11, document.getTotalCharacterCount());
        assertEquals(1, document.getUniqueFormatCount());

        FormattedCharacter first = document.getCharacters().get(0);
        FormattedCharacter last = document.getCharacters().get(10);

        assertSame(first.getFormat(), last.getFormat());
    }

    @Test
    @DisplayName("Distinct formatting creates distinct flyweights in the factory")
    void testDistinctFormattingPool() {
        document.appendString("Title", "Arial", 24, true, false, "#FF0000");
        document.appendString("Body", "Calibri", 11, false, false, "#000000");

        assertEquals(9, document.getTotalCharacterCount());
        assertEquals(2, document.getUniqueFormatCount());

        CharacterFormat titleFormat = document.getCharacters().get(0).getFormat();
        CharacterFormat bodyFormat = document.getCharacters().get(5).getFormat();

        assertNotSame(titleFormat, bodyFormat);
        assertEquals("Arial", titleFormat.getFontName());
        assertEquals("Calibri", bodyFormat.getFontName());
    }

    @Test
    @DisplayName("TextDocument plain text and formatted rendering preserve characters and offsets")
    void testDocumentRendering() {
        document.appendString("Hi", "Arial", 12, true, false, "#000");

        assertEquals("Hi", document.toPlainText());
        String rendered = document.render();

        assertTrue(rendered.contains("[0:'H' {Arial 12pt B #000}]"));
        assertTrue(rendered.contains("[1:'i' {Arial 12pt B #000}]"));
    }

    @Test
    @DisplayName("TextEditorService adds formatted sections and reports metrics")
    void testEditorService() {
        TextEditorService service = new TextEditorService(document);
        service.addHeading1("Header");
        service.addBodyParagraph("Body paragraph text");
        service.addCodeSnippet("int x = 10;");

        // 6 chars (Header) + 19 chars (Body) + 11 chars (Code) = 36 chars
        assertEquals(36, document.getTotalCharacterCount());
        assertEquals(3, document.getUniqueFormatCount());

        String summary = service.printSummary();
        assertTrue(summary.contains("36 characters"));
        assertTrue(summary.contains("Unique Format Flyweights: 3"));
    }

    @Test
    @DisplayName("Factory case insensitivity ensures canonical pooling")
    void testFactoryNormalization() {
        CharacterFormat f1 = factory.getFormat("ARIAL", 12, true, false, "#FFF");
        CharacterFormat f2 = factory.getFormat("arial", 12, true, false, "#fff");

        assertSame(f1, f2);
        assertEquals(1, factory.getFormatCount());
    }
}
