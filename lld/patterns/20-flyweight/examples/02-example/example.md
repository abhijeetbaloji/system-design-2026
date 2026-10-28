# Example 02: Text Editor Formatted Characters

## 1. Problem Scenario
A rich text desktop word processor must support documents containing hundreds of thousands of individual character glyphs.
Each character requires:
- Styling data: font family (`"Calibri"`), font size (`11`), weight (`bold`), style (`italic`), and foreground color (`"#000000"`).
- Document context: glyph character (`'e'`), zero-indexed position offset in the document stream (`position = 4281`).

Without the Flyweight pattern:
- The editor instantiates a `BadFormattedCharacter` for every single character in the document.
- In a 300,000-character document, 300,000 separate font style objects and strings are allocated, consuming hundreds of megabytes of memory for text that uses only 3 or 4 consistent styles.

## 2. Bad Design (Character Format Duplication)
- Every character stores its own independent font and style strings.
- Re-allocates duplicate objects on every keystroke, causing rapid GC pressure.

## 3. Good Design (Flyweight Pattern)
- Separates **Intrinsic State** (`CharacterFormat`: font, size, bold, italic, color) from **Extrinsic State** (`FormattedCharacter`: char glyph, position).
- `CharacterFormat`: Immutable flyweight. Formats and renders a character passed as method parameters (`render(char, position, output)`).
- `CharacterFormatFactory`: Pools `CharacterFormat` instances. For 300,000 characters spanning 4 styles (Body, Heading 1, Subtitle, Code Block), only **4 `CharacterFormat` instances** are ever held in memory!
- `FormattedCharacter`: Extremely slim context object storing a primitive `char`, a primitive `int`, and a pointer to the shared format.

## 4. Class Diagram
See [diagram.mmd](file:///Users/abhijeet/Desktop/system-design-2026/lld/patterns/20-flyweight/examples/02-example/diagram.mmd).

## 5. Execution Flow
1. Client initializes `TextDocument`.
2. Client appends heading text: `"Design Patterns in Java"`.
3. `TextDocument` requests the `"Arial_24_true_false_#0000FF"` format from `CharacterFormatFactory`.
4. The factory creates it once and caches it. All 23 characters of the title share that single format.
5. Client appends 5,000 words of body text in `"Calibri_11_false_false_#000000"`.
6. All 25,000+ body characters share a single format reference.
7. `document.render()` emits styled character stream.
