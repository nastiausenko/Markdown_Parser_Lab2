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

    public String parse(String file) {
        file = removePreformattedText(file);
        file = processInlineElements(file);
        file = setPreformattedText(file);
        return file;
    }

    private String processInlineElements(String html) {
        MarkupChecker markupChecker = new MarkupChecker();
        List<String> boldBlocks = getMatchPatternList(BOLD_REGEX, html);
        List<String> monospacedBlocks = getMatchPatternList(MONOSPACED_REGEX, html);
        List<String> italicBlocks = getMatchPatternList(ITALIC_REGEX, html);

        markupChecker.checkUnpairedMarkup(html);
        markupChecker.checkNested(BOLD_REGEX, ITALIC_REGEX, monospacedBlocks);
        markupChecker.checkNested(BOLD_REGEX, MONOSPACED_REGEX, italicBlocks);
        markupChecker.checkNested(ITALIC_REGEX, MONOSPACED_REGEX, boldBlocks);

        html = html.replaceAll(BOLD_REGEX, "<b>$1</b>");
        html = html.replaceAll(ITALIC_REGEX, "<i>$1</i>");
        html = html.replaceAll(MONOSPACED_REGEX, "<tt>$1</tt>");
        html = setParagraphs(html);
        return html;
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

    private String removePreformattedText(String text) {
        Pattern preformattedPattern = Pattern.compile(PREFORMATTED_REGEX, Pattern.DOTALL);
        Matcher matcher = preformattedPattern.matcher(text);
        while (matcher.find()) {
            String preformattedBlock = matcher.group();
            if (!preformattedBlock.matches("(?s)```\\s*\n.*?\n```")) {
                throw new MarkdownException("ERROR: invalid preformatted text");
            }
            preformattedText.add(matcher.group());
        }
        return text.replaceAll(PREFORMATTED_REGEX, "PRE");
    }

    private String setPreformattedText(String text) {
        for (String cur : preformattedText) {
            String html = "<pre>" + cur.replaceAll("```", "") + "</pre>";
            text = text.replaceFirst("PRE", html);
        }
        return text;
    }

    private String setParagraphs(String text) {
        String[] paragraphs = text.split(System.lineSeparator());
        StringBuilder result = new StringBuilder();
        for (String paragraph : paragraphs) {
            if (!paragraph.isEmpty()) {
                result.append("<p>").append(paragraph.trim()).append("</p>").append(System.lineSeparator());
            }
        }
        return result.toString().trim();
    }
}
