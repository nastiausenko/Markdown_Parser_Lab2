package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MarkdownParser {
    static final String BOLD_REGEX = "(?<![\\w`*\u0400-\u04FF])\\*\\*(\\S(?:.*?\\S)?)\\*\\*(?![\\w`*\u0400-\u04FF])";
    static final String ITALIC_REGEX = "(?<![\\w`*\\u0400-\\u04FF])_(\\S(?:.*?\\S)?)_(?![\\w`*\\u0400-\\u04FF])";
    static final String MONOSPACED_REGEX = "(?<![\\w`*\\u0400-\\u04FF])`(\\S(?:.*?\\S)?)`(?![\\w`*\\u0400-\\u04FF])";
    private static final String PREFORMATTED_REGEX = "```([\\s\\S]*?)```";
    private final List<String> preformattedText = new ArrayList<>();
    private String content;

    public MarkdownParser(String content) {
        this.content = content;
    }

    public String parse(Format format) {
        content = removePreformattedText();
        content = processInlineElements(format);
        content = setPreformattedText(format);
        return content;
    }

    private String processInlineElements(Format format) {
        MarkupChecker markupChecker = new MarkupChecker();
        List<String> boldBlocks = getMatchPatternList(BOLD_REGEX, content);
        List<String> monospacedBlocks = getMatchPatternList(MONOSPACED_REGEX, content);
        List<String> italicBlocks = getMatchPatternList(ITALIC_REGEX, content);

        markupChecker.checkUnpairedMarkup(content);
        markupChecker.checkNested(BOLD_REGEX, ITALIC_REGEX, monospacedBlocks);
        markupChecker.checkNested(BOLD_REGEX, MONOSPACED_REGEX, italicBlocks);
        markupChecker.checkNested(ITALIC_REGEX, MONOSPACED_REGEX, boldBlocks);

        content = setFormat(format);
        return content;
    }

    private String setFormat(Format format) {
        if (format.equals(Format.ANSI)) {
            content = content.replaceAll(BOLD_REGEX, "\u001B[1m$1\u001B[22m");
            content = content.replaceAll(ITALIC_REGEX, "\u001B[3m$1\u001B[23m");
            content = content.replaceAll(MONOSPACED_REGEX, "\u001B[7m$1\u001B[27m");
        } else {
            content = content.replaceAll(BOLD_REGEX, "<b>$1</b>");
            content = content.replaceAll(ITALIC_REGEX, "<i>$1</i>");
            content = content.replaceAll(MONOSPACED_REGEX, "<tt>$1</tt>");
        }
        content = setParagraphs(format);
        return content;
    }

    private List<String> getMatchPatternList(String regex, String html) {
        List<String> regexList = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            regexList.add(matcher.group(1));
        }
        return regexList;
    }

    private String removePreformattedText() {
        Pattern preformattedPattern = Pattern.compile(PREFORMATTED_REGEX, Pattern.DOTALL);
        Matcher matcher = preformattedPattern.matcher(content);
        while (matcher.find()) {
            String preformattedBlock = matcher.group();
            if (!preformattedBlock.matches("(?s)```\\s*\n.*?\n```")) {
                throw new MarkdownException("ERROR: invalid preformatted text");
            }
            preformattedText.add(matcher.group());
        }
        return content.replaceAll(PREFORMATTED_REGEX, "PRE");
    }

    private String setPreformattedText(Format format) {
        for (String cur : preformattedText) {
            String formattedText;
            if (format.equals(Format.ANSI)) {
                formattedText = cur.replaceAll("```", "").trim();
            } else {
                formattedText = "<pre>" + cur.replaceAll("```", "") + "</pre>";
            }
            content = content.replaceFirst("PRE", formattedText);
        }
        return content;
    }

    private String setParagraphs(Format format) {
        String[] paragraphs = content.split("\\R");
        StringBuilder result = new StringBuilder();
        for (String paragraph : paragraphs) {
            if (!paragraph.isEmpty()) {
                if (format.equals(Format.ANSI)) {
                    result.append(paragraph).append("\n");
                } else result.append("<p>").append(paragraph.trim()).append("</p>").append("\n");
            }
        }
        return result.toString().trim();
    }
}
