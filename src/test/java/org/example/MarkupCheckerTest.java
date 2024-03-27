package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MarkupCheckerTest {
    private MarkupChecker markupChecker;

    @BeforeEach
    public void beforeEach() {
        markupChecker = new MarkupChecker();
    }

    @ParameterizedTest
    @ValueSource(strings = {"_text", "text_", "**text", "text**", "`text", "text`"})
    void checkUnpairedMarkupTest(String input) {
       MarkdownException exception = Assertions.assertThrows(MarkdownException.class,
               ()-> markupChecker.checkUnpairedMarkup(input));
       Assertions.assertTrue(exception.getMessage().contains("Error: unpaired markup"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"_**text", "text**_", "**_text", "text_**", "`**text", "text**`", "```\ntext", "text\n```"})
    void checkUnpairedNestedMarkupTest(String input) {
        MarkdownException exception = Assertions.assertThrows(MarkdownException.class,
                ()-> markupChecker.checkUnpairedMarkup(input));
        Assertions.assertTrue(exception.getMessage().contains("ERROR: unpaired nested markup"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"**text\n**", "**\ntext**", "_\ntext_", "_text\n_", "`\ntext`", "`text\n`"})
    void checkUnpairedNewlineMarkupTest(String input) {
        MarkdownException exception = Assertions.assertThrows(MarkdownException.class,
                ()-> markupChecker.checkUnpairedMarkup(input));
        Assertions.assertTrue(exception.getMessage().contains("Error: unpaired markup"));
    }
}
