package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MarkdownParserTest {
    private MarkdownParser markdownParser;

    @ParameterizedTest
    @CsvSource({
            "**text**,<p><b>text</b></p>",
            "_text_,<p><i>text</i></p>",
            "`text`,<p><tt>text</tt></p>"
    })
    void markdownFormattingTest(String input, String expected) {
        markdownParser = new MarkdownParser(input);
        Assertions.assertEquals(expected, markdownParser.parse(Format.HTML));
    }

    @Test
    void preformattedMarkdownTest() {
        String input = """
                ```
                _text_
                ```""";
        String expected = """
                <p><pre>
                _text_
                </pre></p>""";

        markdownParser = new MarkdownParser(input);
        Assertions.assertEquals(expected, markdownParser.parse(Format.HTML));
    }

    @Test
    void paragraphMarkdownTest() {
        String input = """
                text
                more text
             
                and more text""";
        String expected = """
                <p>text</p>
                <p>more text</p>
                <p>and more text</p>""";
        markdownParser = new MarkdownParser(input);
        Assertions.assertEquals(expected, markdownParser.parse(Format.HTML));
    }

    @ParameterizedTest
    @CsvSource({
            "** text,<p>** text</p>",
            "_ text _,<p>_ text _</p>",
            "`**` text ` `_`,<p><tt>**</tt> text ` <tt>_</tt></p>"
    })
    void separateMarkupTest(String input, String expected) {
        markdownParser = new MarkdownParser(input);
        Assertions.assertEquals(expected, markdownParser.parse(Format.HTML));
    }

    @ParameterizedTest
    @CsvSource({
            "t**ext,<p>t**ext</p>",
            "te_xt,<p>te_xt</p>",
            "te`_xt,<p>te`_xt</p>"
    })
    void insideTextMarkupTest(String input, String expected) {
        markdownParser = new MarkdownParser(input);
        Assertions.assertEquals(expected, markdownParser.parse(Format.HTML));
    }

    @Test
    void invalidPreformattedTest() {
        String input = """
                ```text
                ```""";
        markdownParser = new MarkdownParser(input);
        MarkdownException exception = Assertions.assertThrows(MarkdownException.class,
                ()-> markdownParser.parse(Format.HTML));
        Assertions.assertTrue(exception.getMessage().contains("ERROR: invalid preformatted text"));
    }

    @Test
    void invalidPreformatted2Test() {
        String input = """
                ```
                text```""";
        markdownParser = new MarkdownParser(input);
        MarkdownException exception = Assertions.assertThrows(MarkdownException.class,
                ()-> markdownParser.parse(Format.HTML));
        Assertions.assertTrue(exception.getMessage().contains("ERROR: invalid preformatted text"));
    }

    @Test
    void markdownANSIBoldTest() {
        markdownParser = new MarkdownParser("**text**");
        String expected = """
                \u001B[1mtext\u001B[22m""";
        Assertions.assertEquals(expected, markdownParser.parse(Format.ANSI));
    }

    @Test
    void markdownANSIMonospacedTest() {
        markdownParser = new MarkdownParser("`text`");
        String expected = """
                \u001B[7mtext\u001B[27m""";
        Assertions.assertEquals(expected, markdownParser.parse(Format.ANSI));
    }

    @Test
    void markdownANSIItalicTest() {
        markdownParser = new MarkdownParser("_text_");
        String expected = """
                \u001B[3mtext\u001B[23m""";
        Assertions.assertEquals(expected, markdownParser.parse(Format.ANSI));
    }

    @Test
    void paragraphMarkdownANSITest() {
        String input = """
                text
                
                more text
                and more text""";

        String expected = """
                text
                more text
                and more text""";
        markdownParser = new MarkdownParser(input);
        Assertions.assertEquals(expected, markdownParser.parse(Format.ANSI));
    }

    @ParameterizedTest
    @CsvSource({
            "t**ext,t**ext",
            "te_xt,te_xt",
            "te`_xt,te`_xt"
    })
    void insideTextMarkupANSITest(String input, String expected) {
        markdownParser = new MarkdownParser(input);
        Assertions.assertEquals(expected, markdownParser.parse(Format.ANSI));
    }

    @ParameterizedTest
    @CsvSource({
            "** text,** text",
            "_ text _,_ text _",
            "` text `,` text `"
    })
    void separateMarkupANSITest(String input, String expected) {
        markdownParser = new MarkdownParser(input);
        Assertions.assertEquals(expected, markdownParser.parse(Format.ANSI));
    }

    @Test
    void separateMarkupANSITest() {
        markdownParser = new MarkdownParser("`**` text ` `_`");
        String expected = """
                \u001B[7m**\u001B[27m text ` \u001B[7m_\u001B[27m""";
        Assertions.assertEquals(expected, markdownParser.parse(Format.ANSI));
    }

    @Test
    void preformattedMarkupANSITest() {
        String input = """
                ```
                _text_
                
                **more text**
                `and more_text
                ```""";
        String expected = """
                _text_
                
                **more text**
                `and more_text""";

        markdownParser = new MarkdownParser(input);
        Assertions.assertEquals(expected, markdownParser.parse(Format.ANSI));
    }
}
