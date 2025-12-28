package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для проверки класса Element и его наследников.
 */
class ElementTest {
    @Test
    void testText() {
        Text text = Text.of("Hello");
        System.out.println(text.hashCode());
        assertEquals("Hello", text.mdFormat());
        assertEquals(Text.of("Hello"), text);
        assertNotEquals(Text.of("Hello"), Text.of("World"));
    }

    @Test
    void testBoldFormat() {
        Text text = Text.of("text");
        Format bold = Format.bold(text);
        assertEquals("**text**", bold.mdFormat());
        assertEquals(Format.bold(text), bold);
    }

    @Test
    void testItalicFormat() {
        Text text = Text.of("text");
        Format italic = Format.italic(text);
        assertEquals("*text*", italic.mdFormat());
    }

    @Test
    void testStrikeFormat() {
        Text text = Text.of("text");
        Format strike = Format.strike(text);
        assertEquals("~~text~~", strike.mdFormat());
    }

    @Test
    void testCodeFormat() {
        Text text = Text.of("var");
        Format code = Format.code(text);
        assertEquals("`var`", code.mdFormat());
    }

    @Test
    void testHeader() {
        Text text = Text.of("Title");
        Header header = Header.of(1, text);
        assertFalse(header.equals(Header.of(2, text)));
        assertEquals("# Title", header.mdFormat());

        Header header3 = Header.of(3, text);
        assertEquals("### Title", header3.mdFormat());
        assertNotEquals(header.hashCode(), header3.hashCode());

        assertThrows(IllegalArgumentException.class, () -> Header.of(0, text));
        assertThrows(IllegalArgumentException.class, () -> Header.of(7, text));
    }

    @Test
    void testLink() {
        Link link = Link.of("Google", "https://google.com");
        assertEquals("[Google](https://google.com)", link.mdFormat());
    }

    @Test
    void testImage() {
        Image image = Image.of("Logo", "/path/to/logo.png");
        assertEquals("![Logo](/path/to/logo.png)", image.mdFormat());
    }

    @Test
    void testQuote() {
        Text text = Text.of("Cogito ergo sum");
        Quote quote = Quote.of(text);
        System.out.println(quote.hashCode());
        assertEquals("> Cogito ergo sum", quote.mdFormat());
        assertEquals(Quote.of(text), quote);
    }

    @Test
    void testUnorderedList() {
        ListElement list = new ListElement.Builder()
                .add(Text.of("First item"))
                .add(Text.of("Second item"))
                .add(Text.of("Third item"))
                .build();

        String expected = "* First item\n* Second item\n* Third item\n";
        assertEquals(expected, list.mdFormat());
    }

    @Test
    void testOrderedList() {
        ListElement list = new ListElement.Builder()
                .ordered()
                .add(Text.of("First"))
                .add(Text.of("Second"))
                .add(Text.of("Third"))
                .build();

        String expected = "1. First\n2. Second\n3. Third\n";
        assertEquals(expected, list.mdFormat());
    }

    @Test
    void testTaskList() {
        TaskList task1 = TaskList.of(false, "Write tests");
        assertEquals("- [ ]Write tests", task1.mdFormat());

        TaskList task2 = TaskList.of(true, "Write code");
        assertEquals("- [x]Write code", task2.mdFormat());
    }

    @Test
    void testCodeBlock() {
        CodeBlock codeBlock = CodeBlock.of("java", "public class Test {}");
        String expected = "```java\npublic class Test {}\n```";
        assertEquals(expected, codeBlock.mdFormat());
    }

    @Test
    void testTable() {
        Table table = new Table.Builder()
                .header("Name", "Age", "City")
                .align(Table.Align.LEFT, Table.Align.CENTER, Table.Align.RIGHT)
                .row("Alice", "25", "New York")
                .row("Bob", "30", "London")
                .build();

        String result = table.mdFormat();
        assertTrue(result.contains("| Name  | Age |     City |"));
        assertTrue(result.contains("| :---- | :-: | -------: |"));
        assertTrue(result.contains("| Alice | 25  | New York |"));
        assertTrue(result.contains("| Bob   | 30  |   London |"));
    }

    @Test
    void testTableValidation() {
        assertThrows(IllegalStateException.class, () ->
                new Table.Builder().build());

        assertThrows(IllegalStateException.class, () ->
                new Table.Builder()
                        .header("A", "B")
                        .align(Table.Align.LEFT)
                        .row("1", "2")
                        .build());
    }

    @Test
    void testNestedElements() {
        Text text = Text.of("text");
        Format bold = Format.bold(text);
        Quote quote = Quote.of(bold);

        assertEquals("> **text**", quote.mdFormat());
    }

    @Test
    void testListWithComplexItems() {
        Text item1 = Text.of("text");
        Text item2 = Text.of("important");
        Format boldItem = Format.bold(item2);

        ListElement list = new ListElement.Builder()
                .add(item1)
                .add(boldItem)
                .build();

        String expected = "* text\n* **important**\n";
        assertEquals(expected, list.mdFormat());
    }

    @Test
    void testToString() {
        Text text = Text.of("Test");
        assertEquals("Test", text.toString());

        Format bold = Format.bold(text);
        assertEquals("**Test**", bold.toString());
    }
}
