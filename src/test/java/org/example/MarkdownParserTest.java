package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MarkdownParserTest {
    private MarkdownParser markdownParser;

    @BeforeEach
    public void beforeEach() {
        markdownParser = new MarkdownParser();
    }

    @ParameterizedTest
    @CsvSource({
            "**text**,<p><b>text</b></p>",
            "_text_,<p><i>text</i></p>",
            "`text`,<p><tt>text</tt></p>"
    })
    void markdownFormattingTest(String input, String expected) {
        String result = markdownParser.parse(input);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void preformattedMarkdownTest() {
        String result = markdownParser.parse("""
                ```
                _text_
                ```""");
        String expected = """
                <p><pre>
                _text_
                </pre></p>""";

        Assertions.assertEquals(expected,result);
    }

    @Test
    void paragraphMarkdownTest() {
        String result = markdownParser.parse("text" +
                System.lineSeparator() +
                "more text" +
                System.lineSeparator() +
                "and more text");

        String expected = "<p>text</p>" +
                System.lineSeparator() +
                "<p>more text</p>" +
                System.lineSeparator() +
                "<p>and more text</p>";

        Assertions.assertEquals(expected, result);
    }
}
